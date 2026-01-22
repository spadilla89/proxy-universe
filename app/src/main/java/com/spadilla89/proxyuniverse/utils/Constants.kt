package com.spadilla89.proxyuniverse.utils

object Constants {
    // API URLs
    const val PROXYSCRAPE_API_URL = "https://api.proxyscrape.com/v2/"
    const val GEONODE_API_URL = "https://proxylist.geonode.com/api/"
    const val PUBPROXY_API_URL = "http://pubproxy.com/api/"
    
    // Scraper URLs
    const val FREE_PROXY_LIST_URL = "https://free-proxy-list.net/"
    const val SSL_PROXIES_URL = "https://www.sslproxies.org/"
    const val SOCKS_PROXY_URL = "https://www.socks-proxy.net/"
    const val HIDEMY_NAME_URL = "https://hidemy.name/en/proxy-list/"
    const val PROXYNOVA_URL = "https://www.proxynova.com/proxy-server-list/"
    
    // Validation
    const val VALIDATION_TIMEOUT_MS = 5000
    const val VALIDATION_POOL_SIZE = 15
    const val VALIDATION_TEST_URL_HTTP = "http://www.google.com"
    const val VALIDATION_TEST_URL_HTTPS = "https://www.google.com"
    
    // Export
    const val EXPORT_FILE_PREFIX = "universe_proxy_"
    const val EXPORT_FILE_EXTENSION = ".txt"
    const val EXPORT_DATE_FORMAT = "yyyyMMdd_HHmmss"
    
    // Limits
    const val MAX_PROXIES_PER_REQUEST = 500
    const val SCRAPER_TIMEOUT_MS = 10000
    const val API_TIMEOUT_MS = 15000
    
    // SharedPreferences
    const val PREFS_NAME = "universe_proxy_prefs"
    const val PREFS_KEY_SELECTED_COUNTRIES = "selected_countries"
    const val PREFS_KEY_SELECTED_ANONYMITY = "selected_anonymity"
    const val PREFS_KEY_LAST_PROTOCOL = "last_protocol"
}
