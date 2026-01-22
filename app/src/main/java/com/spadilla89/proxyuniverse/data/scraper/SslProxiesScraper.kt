package com.spadilla89.proxyuniverse.data.scraper

import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import org.jsoup.nodes.Document

class SslProxiesScraper : BaseScraper(
    name = "sslproxies.org",
    baseUrl = "https://www.sslproxies.org/"
) {
    override fun parseProxies(document: Document, protocol: ProxyProtocol): List<Proxy> {
        val proxies = mutableListOf<Proxy>()
        
        // Only scrape HTTPS proxies from this site
        if (protocol != ProxyProtocol.HTTPS) {
            return proxies
        }
        
        try {
            val rows = document.select("table.table tbody tr")
            
            for (row in rows) {
                val cells = row.select("td")
                if (cells.size < 7) continue
                
                val ip = cells[0].textOrNull()
                val port = cells[1].textOrNull()
                val countryCode = cells[2].textOrNull()
                val country = cells[3].textOrNull()
                val anonymity = parseAnonymity(cells[4].textOrNull())
                
                createProxy(
                    ip = ip,
                    port = port,
                    protocol = ProxyProtocol.HTTPS,
                    country = country,
                    countryCode = countryCode,
                    anonymity = anonymity
                )?.let { proxies.add(it) }
            }
        } catch (e: Exception) {
            println("Error parsing sslproxies.org: ${e.message}")
        }
        
        return proxies
    }
}
