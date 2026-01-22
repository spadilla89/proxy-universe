package com.spadilla89.proxyuniverse.data.scraper

import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import org.jsoup.nodes.Document

class FreeProxyListScraper : BaseScraper(
    name = "free-proxy-list.net",
    baseUrl = "https://free-proxy-list.net/"
) {
    override fun parseProxies(document: Document, protocol: ProxyProtocol): List<Proxy> {
        val proxies = mutableListOf<Proxy>()
        
        try {
            // Select the table rows
            val rows = document.select("table#proxylisttable tbody tr")
            
            for (row in rows) {
                val cells = row.select("td")
                if (cells.size < 7) continue
                
                val ip = cells[0].textOrNull()
                val port = cells[1].textOrNull()
                val countryCode = cells[2].textOrNull()
                val country = cells[3].textOrNull()
                val anonymity = parseAnonymity(cells[4].textOrNull())
                val https = cells[6].textOrNull()
                
                // Determine protocol based on HTTPS column
                val proxyProtocol = if (https?.lowercase() == "yes") {
                    ProxyProtocol.HTTPS
                } else {
                    ProxyProtocol.HTTP
                }
                
                // Only include if it matches the requested protocol
                if (proxyProtocol == protocol) {
                    createProxy(
                        ip = ip,
                        port = port,
                        protocol = protocol,
                        country = country,
                        countryCode = countryCode,
                        anonymity = anonymity
                    )?.let { proxies.add(it) }
                }
            }
        } catch (e: Exception) {
            println("Error parsing free-proxy-list.net: ${e.message}")
        }
        
        return proxies
    }
}
