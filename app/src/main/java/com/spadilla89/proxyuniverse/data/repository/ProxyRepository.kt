package com.spadilla89.proxyuniverse.data.repository

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.spadilla89.proxyuniverse.data.api.ApiService
import com.spadilla89.proxyuniverse.data.model.AnonymityLevel
import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import com.spadilla89.proxyuniverse.data.scraper.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ProxyRepository {
    
    private val scrapers = listOf(
        FreeProxyListScraper(),
        SslProxiesScraper(),
        SocksProxyScraper(),
        HideMyNameScraper(),
        ProxyNovaScraper()
    )
    
    /**
     * Fetch proxies from all sources (APIs + Scrapers)
     */
    suspend fun fetchProxies(
        protocol: ProxyProtocol,
        countries: List<String>? = null,
        anonymityLevels: List<AnonymityLevel>? = null
    ): List<Proxy> = coroutineScope {
        val allProxies = mutableListOf<Proxy>()
        
        // Fetch from APIs and scrapers in parallel
        val apiResults = async { fetchFromApis(protocol, countries, anonymityLevels) }
        val scraperResults = async { fetchFromScrapers(protocol) }
        
        allProxies.addAll(apiResults.await())
        allProxies.addAll(scraperResults.await())
        
        // Remove duplicates based on IP:PORT
        val uniqueProxies = allProxies
            .distinctBy { it.getUniqueId() }
            .filter { proxy ->
                // Apply filters
                val matchesCountry = countries.isNullOrEmpty() || 
                    countries.any { it.equals(proxy.countryCode, ignoreCase = true) || 
                                   it.equals(proxy.country, ignoreCase = true) }
                
                val matchesAnonymity = anonymityLevels.isNullOrEmpty() || 
                    proxy.anonymity in anonymityLevels
                
                matchesCountry && matchesAnonymity
            }
        
        uniqueProxies
    }
    
    /**
     * Fetch from all API sources
     */
    private suspend fun fetchFromApis(
        protocol: ProxyProtocol,
        countries: List<String>?,
        anonymityLevels: List<AnonymityLevel>?
    ): List<Proxy> = coroutineScope {
        val results = mutableListOf<Proxy>()
        
        // Fetch from all APIs in parallel
        val apiCalls = listOf(
            async { fetchFromProxyScrape(protocol, countries, anonymityLevels) },
            async { fetchFromGeoNode(protocol, countries, anonymityLevels) },
            async { fetchFromPubProxy(protocol, countries, anonymityLevels) }
        )
        
        apiCalls.awaitAll().forEach { proxies ->
            results.addAll(proxies)
        }
        
        results
    }
    
    /**
     * Fetch from ProxyScrape API
     */
    private suspend fun fetchFromProxyScrape(
        protocol: ProxyProtocol,
        countries: List<String>?,
        anonymityLevels: List<AnonymityLevel>?
    ): List<Proxy> {
        return try {
            val protocolParam = when (protocol) {
                ProxyProtocol.HTTP -> "http"
                ProxyProtocol.HTTPS -> "http" // ProxyScrape doesn't separate HTTP/HTTPS
                ProxyProtocol.SOCKS4 -> "socks4"
                ProxyProtocol.SOCKS5 -> "socks5"
            }
            
            val response = ApiService.proxyScrapeApi.getProxies(
                protocol = protocolParam,
                timeout = 10000
            )
            
            if (response.isSuccessful && response.body() != null) {
                parseProxyScrapeResponse(response.body()!!, protocol)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error fetching from ProxyScrape: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Parse ProxyScrape API response (plain text format)
     */
    private fun parseProxyScrapeResponse(body: String, protocol: ProxyProtocol): List<Proxy> {
        val proxies = mutableListOf<Proxy>()
        
        try {
            // ProxyScrape returns plain text with IP:PORT per line
            body.lines().forEach { line ->
                val trimmed = line.trim()
                if (trimmed.isNotEmpty() && !trimmed.startsWith("#")) {
                    Proxy.fromString(
                        ipPort = trimmed,
                        protocol = protocol,
                        source = "API: ProxyScrape"
                    )?.let { proxies.add(it) }
                }
            }
        } catch (e: Exception) {
            println("Error parsing ProxyScrape response: ${e.message}")
        }
        
        return proxies
    }
    
    /**
     * Fetch from GeoNode API
     */
    private suspend fun fetchFromGeoNode(
        protocol: ProxyProtocol,
        countries: List<String>?,
        anonymityLevels: List<AnonymityLevel>?
    ): List<Proxy> {
        return try {
            val protocolParam = when (protocol) {
                ProxyProtocol.HTTP -> "http"
                ProxyProtocol.HTTPS -> "https"
                ProxyProtocol.SOCKS4 -> "socks4"
                ProxyProtocol.SOCKS5 -> "socks5"
            }
            
            val response = ApiService.geoNodeApi.getProxies(
                protocols = protocolParam,
                limit = 500
            )
            
            if (response.isSuccessful && response.body() != null) {
                val geoNodeResponse = response.body()!!
                geoNodeResponse.data?.mapNotNull { geoProxy ->
                    val ip = geoProxy.ip ?: return@mapNotNull null
                    val port = geoProxy.port?.toIntOrNull() ?: return@mapNotNull null
                    
                    Proxy(
                        ip = ip,
                        port = port,
                        protocol = protocol,
                        country = geoProxy.country ?: "Unknown",
                        countryCode = "",
                        anonymity = AnonymityLevel.fromString(geoProxy.anonymityLevel ?: ""),
                        speed = geoProxy.responseTime,
                        source = "API: GeoNode"
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error fetching from GeoNode: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Fetch from PubProxy API
     */
    private suspend fun fetchFromPubProxy(
        protocol: ProxyProtocol,
        countries: List<String>?,
        anonymityLevels: List<AnonymityLevel>?
    ): List<Proxy> {
        return try {
            val typeParam = when (protocol) {
                ProxyProtocol.HTTP -> "http"
                ProxyProtocol.HTTPS -> "https"
                ProxyProtocol.SOCKS4 -> "socks4"
                ProxyProtocol.SOCKS5 -> "socks5"
            }
            
            val response = ApiService.pubProxyApi.getProxies(
                type = typeParam,
                limit = 20
            )
            
            if (response.isSuccessful && response.body() != null) {
                val pubProxyResponse = response.body()!!
                pubProxyResponse.data?.mapNotNull { pubProxy ->
                    val ipPort = pubProxy.ipPort ?: pubProxy.ip?.let { ip ->
                        pubProxy.port?.let { port -> "$ip:$port" }
                    } ?: return@mapNotNull null
                    
                    Proxy.fromString(
                        ipPort = ipPort,
                        protocol = protocol,
                        country = pubProxy.country ?: "Unknown",
                        anonymity = AnonymityLevel.fromString(pubProxy.level ?: ""),
                        source = "API: PubProxy"
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error fetching from PubProxy: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * Fetch from all web scrapers
     */
    private suspend fun fetchFromScrapers(protocol: ProxyProtocol): List<Proxy> = coroutineScope {
        val results = mutableListOf<Proxy>()
        
        // Run all scrapers in parallel
        val scraperJobs = scrapers.map { scraper ->
            async {
                try {
                    scraper.scrapeProxies(protocol)
                } catch (e: Exception) {
                    println("Error in scraper: ${e.message}")
                    emptyList()
                }
            }
        }
        
        scraperJobs.awaitAll().forEach { proxies ->
            results.addAll(proxies)
        }
        
        results
    }
}
