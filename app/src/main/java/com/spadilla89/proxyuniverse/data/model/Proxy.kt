package com.spadilla89.proxyuniverse.data.model

data class Proxy(
    val ip: String,
    val port: Int,
    val protocol: ProxyProtocol,
    val country: String = "Unknown",
    val countryCode: String = "",
    val anonymity: AnonymityLevel? = null,
    var speed: Int? = null, // Response time in milliseconds
    var isWorking: Boolean? = null, // null = not verified, true = working, false = not working
    var lastChecked: Long? = null, // Timestamp
    val source: String = "Unknown" // e.g., "API: ProxyScrape" or "Scraper: free-proxy-list.net"
) {
    /**
     * Returns the proxy in the format IP:PORT
     */
    fun toFormattedString(): String = "$ip:$port"

    /**
     * Returns a unique identifier for deduplication
     */
    fun getUniqueId(): String = "${ip.lowercase()}:$port"

    /**
     * Creates a copy with updated validation status
     */
    fun withValidationResult(working: Boolean, responseTime: Int?): Proxy {
        return copy(
            isWorking = working,
            speed = responseTime,
            lastChecked = System.currentTimeMillis()
        )
    }

    companion object {
        /**
         * Creates a Proxy from a simple IP:PORT string
         */
        fun fromString(
            ipPort: String,
            protocol: ProxyProtocol,
            country: String = "Unknown",
            countryCode: String = "",
            anonymity: AnonymityLevel? = null,
            source: String = "Unknown"
        ): Proxy? {
            val parts = ipPort.trim().split(":")
            if (parts.size != 2) return null
            
            val ip = parts[0].trim()
            val port = parts[1].trim().toIntOrNull() ?: return null
            
            if (!isValidIp(ip) || !isValidPort(port)) return null
            
            return Proxy(
                ip = ip,
                port = port,
                protocol = protocol,
                country = country,
                countryCode = countryCode,
                anonymity = anonymity,
                source = source
            )
        }

        /**
         * Validates IP address format
         */
        private fun isValidIp(ip: String): Boolean {
            val parts = ip.split(".")
            if (parts.size != 4) return false
            return parts.all { 
                it.toIntOrNull()?.let { num -> num in 0..255 } == true 
            }
        }

        /**
         * Validates port number
         */
        private fun isValidPort(port: Int): Boolean {
            return port in 1..65535
        }
    }
}
