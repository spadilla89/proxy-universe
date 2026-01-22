package com.spadilla89.proxyuniverse.data.scraper

import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import com.spadilla89.proxyuniverse.data.model.AnonymityLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

abstract class BaseScraper(
    protected val name: String,
    protected val baseUrl: String
) {
    protected open val timeout: Int = 10000 // 10 seconds
    
    /**
     * Fetch and parse proxies from the website
     */
    suspend fun scrapeProxies(protocol: ProxyProtocol): List<Proxy> {
        return withContext(Dispatchers.IO) {
            try {
                val document = fetchDocument()
                parseProxies(document, protocol)
            } catch (e: Exception) {
                println("Error scraping $name: ${e.message}")
                emptyList()
            }
        }
    }
    
    /**
     * Fetch the HTML document
     */
    protected open suspend fun fetchDocument(): Document {
        return withContext(Dispatchers.IO) {
            Jsoup.connect(baseUrl)
                .timeout(timeout)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .get()
        }
    }
    
    /**
     * Parse proxies from the document
     * Must be implemented by each scraper
     */
    protected abstract fun parseProxies(document: Document, protocol: ProxyProtocol): List<Proxy>
    
    /**
     * Helper to safely extract text from an element
     */
    protected fun Element?.textOrNull(): String? {
        return this?.text()?.trim()?.takeIf { it.isNotEmpty() }
    }
    
    /**
     * Helper to parse anonymity level from various formats
     */
    protected fun parseAnonymity(text: String?): AnonymityLevel? {
        if (text == null) return null
        return AnonymityLevel.fromString(text)
    }
    
    /**
     * Helper to create proxy from parsed data
     */
    protected fun createProxy(
        ip: String?,
        port: String?,
        protocol: ProxyProtocol,
        country: String? = null,
        countryCode: String? = null,
        anonymity: AnonymityLevel? = null
    ): Proxy? {
        if (ip == null || port == null) return null
        
        val portNum = port.toIntOrNull() ?: return null
        
        return Proxy.fromString(
            ipPort = "$ip:$portNum",
            protocol = protocol,
            country = country ?: "Unknown",
            countryCode = countryCode ?: "",
            anonymity = anonymity,
            source = "Scraper: $name"
        )
    }
}
