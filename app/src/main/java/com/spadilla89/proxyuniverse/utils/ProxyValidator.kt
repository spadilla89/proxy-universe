package com.spadilla89.proxyuniverse.utils

import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URL
import java.net.HttpURLConnection

object ProxyValidator {
    
    /**
     * Validate a single proxy
     */
    suspend fun validateProxy(proxy: Proxy): Proxy {
        return withContext(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            
            try {
                val isWorking = when (proxy.protocol) {
                    ProxyProtocol.HTTP, ProxyProtocol.HTTPS -> validateHttpProxy(proxy)
                    ProxyProtocol.SOCKS4, ProxyProtocol.SOCKS5 -> validateSocksProxy(proxy)
                }
                
                val responseTime = if (isWorking) {
                    (System.currentTimeMillis() - startTime).toInt()
                } else {
                    null
                }
                
                proxy.withValidationResult(isWorking, responseTime)
            } catch (e: Exception) {
                proxy.withValidationResult(false, null)
            }
        }
    }
    
    /**
     * Validate multiple proxies in parallel
     */
    suspend fun validateProxies(
        proxies: List<Proxy>,
        onProgress: ((Int, Int) -> Unit)? = null
    ): List<Proxy> {
        return withContext(Dispatchers.IO) {
            val total = proxies.size
            var completed = 0
            
            proxies.chunked(Constants.VALIDATION_POOL_SIZE).flatMap { chunk ->
                chunk.map { proxy ->
                    async {
                        val result = validateProxy(proxy)
                        completed++
                        onProgress?.invoke(completed, total)
                        result
                    }
                }.awaitAll()
            }
        }
    }
    
    /**
     * Validate HTTP/HTTPS proxy by attempting connection
     */
    private fun validateHttpProxy(proxy: Proxy): Boolean {
        var socket: Socket? = null
        try {
            // First try to connect to the proxy server
            socket = Socket()
            val address = InetSocketAddress(proxy.ip, proxy.port)
            socket.connect(address, Constants.VALIDATION_TIMEOUT_MS)
            
            // If connection successful, proxy is reachable
            return socket.isConnected
        } catch (e: Exception) {
            return false
        } finally {
            try {
                socket?.close()
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
    
    /**
     * Validate SOCKS proxy by attempting connection
     */
    private fun validateSocksProxy(proxy: Proxy): Boolean {
        var socket: Socket? = null
        try {
            socket = Socket()
            val address = InetSocketAddress(proxy.ip, proxy.port)
            socket.connect(address, Constants.VALIDATION_TIMEOUT_MS)
            return socket.isConnected
        } catch (e: Exception) {
            return false
        } finally {
            try {
                socket?.close()
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
    
    /**
     * Test proxy by making actual HTTP request through it
     * This is more thorough but slower
     */
    @Suppress("unused")
    private fun testProxyWithRequest(proxy: Proxy): Boolean {
        var connection: HttpURLConnection? = null
        try {
            val url = URL(Constants.VALIDATION_TEST_URL_HTTP)
            val proxyObj = java.net.Proxy(
                java.net.Proxy.Type.HTTP,
                InetSocketAddress(proxy.ip, proxy.port)
            )
            
            connection = url.openConnection(proxyObj) as HttpURLConnection
            connection.connectTimeout = Constants.VALIDATION_TIMEOUT_MS
            connection.readTimeout = Constants.VALIDATION_TIMEOUT_MS
            connection.requestMethod = "GET"
            
            val responseCode = connection.responseCode
            return responseCode in 200..399
        } catch (e: Exception) {
            return false
        } finally {
            connection?.disconnect()
        }
    }
}
