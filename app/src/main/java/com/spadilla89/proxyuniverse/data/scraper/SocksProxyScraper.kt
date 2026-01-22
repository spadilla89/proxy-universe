package com.spadilla89.proxyuniverse.data.scraper

import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import org.jsoup.nodes.Document

class SocksProxyScraper : BaseScraper(
    name = "socks-proxy.net",
    baseUrl = "https://www.socks-proxy.net/"
) {
    override fun parseProxies(document: Document, protocol: ProxyProtocol): List<Proxy> {
        val proxies = mutableListOf<Proxy>()
        
        // This site has SOCKS4 and SOCKS5 proxies
        if (protocol != ProxyProtocol.SOCKS4 && protocol != ProxyProtocol.SOCKS5) {
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
                val versionText = cells[4].textOrNull()
                val anonymity = parseAnonymity(cells[5].textOrNull())
                
                // Parse SOCKS version
                val socksProtocol = when {
                    versionText?.contains("5", ignoreCase = true) == true -> ProxyProtocol.SOCKS5
                    versionText?.contains("4", ignoreCase = true) == true -> ProxyProtocol.SOCKS4
                    else -> null
                }
                
                // Only include if it matches the requested protocol
                if (socksProtocol == protocol) {
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
            println("Error parsing socks-proxy.net: ${e.message}")
        }
        
        return proxies
    }
}
