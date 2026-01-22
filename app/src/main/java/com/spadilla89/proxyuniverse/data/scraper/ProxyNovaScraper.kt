package com.spadilla89.proxyuniverse.data.scraper

import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import org.jsoup.nodes.Document

class ProxyNovaScraper : BaseScraper(
    name = "proxynova.com",
    baseUrl = "https://www.proxynova.com/proxy-server-list/"
) {
    override fun parseProxies(document: Document, protocol: ProxyProtocol): List<Proxy> {
        val proxies = mutableListOf<Proxy>()
        
        try {
            val rows = document.select("table#tbl_proxy_list tbody tr")
            
            for (row in rows) {
                val cells = row.select("td")
                if (cells.size < 6) continue
                
                // ProxyNova uses JavaScript to obfuscate IPs
                // We'll try to extract the visible IP
                val ipElement = cells[0].select("abbr").firstOrNull()
                val ip = ipElement?.attr("title")?.trim() ?: cells[0].textOrNull()
                val port = cells[1].textOrNull()
                val country = cells[5].textOrNull()
                
                // ProxyNova mainly lists HTTP/HTTPS proxies
                if (protocol == ProxyProtocol.HTTP || protocol == ProxyProtocol.HTTPS) {
                    createProxy(
                        ip = ip,
                        port = port,
                        protocol = protocol,
                        country = country
                    )?.let { proxies.add(it) }
                }
            }
        } catch (e: Exception) {
            println("Error parsing proxynova.com: ${e.message}")
        }
        
        return proxies
    }
}
