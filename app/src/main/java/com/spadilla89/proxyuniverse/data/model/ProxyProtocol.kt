package com.spadilla89.proxyuniverse.data.model

enum class ProxyProtocol(val displayName: String) {
    HTTP("HTTP"),
    HTTPS("HTTPS"),
    SOCKS4("SOCKS4"),
    SOCKS5("SOCKS5");

    companion object {
        fun fromString(value: String): ProxyProtocol? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}
