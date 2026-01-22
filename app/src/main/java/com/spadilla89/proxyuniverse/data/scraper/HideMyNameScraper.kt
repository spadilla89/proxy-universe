package com.spadilla89.proxyuniverse.data.scraper

import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import org.jsoup.nodes.Document

class HideMyNameScraper : BaseScraper(
    name = "hidemy.name",
    baseUrl = "https://hidemy.name/en/proxy-list/"
) {
    override fun parseProxies(document: Document, protocol: ProxyProtocol): List<Proxy> {
        val proxies = mutableListOf<Proxy>()
        
        try {
            val rows = document.select("table tbody tr")
            
            for (row in rows) {
                val cells = row.select("td")
                if (cells.size < 5) continue
                
                val ip = cells[0].textOrNull()
                val port = cells[1].textOrNull()
                val country = cells[2].textOrNull()
                val typeText = cells[4].textOrNull()
                val anonymityText = cells[5]?.textOrNull()
                
                // Parse protocol type
                val proxyProtocol = when {
                    typeText?.contains("SOCKS5", ignoreCase = true) == true -> ProxyProtocol.SOCKS5
                    typeText?.contains("SOCKS4", ignoreCase = true) == true -> ProxyProtocol.SOCKS4
                    typeText?.contains("HTTPS", ignoreCase = true) == true -> ProxyProtocol.HTTPS
                    typeText?.contains("HTTP", ignoreCase = true) == true -> ProxyProtocol.HTTP
                    else -> null
                }
                
                // Only include if it matches the requested protocol
                if (proxyProtocol == protocol) {
                    createProxy(
                        ip = ip,
                        port = port,
                        protocol = protocol,
                        country = country,
                        anonymity = parseAnonymity(anonymityText)
                    )?.let { proxies.add(it) }
                }
            }
        } catch (e: Exception) {
            println("Error parsing hidemy.name: ${e.message}")
        }
        
        return proxies
    }
}
