package com.spadilla89.proxyuniverse.utils

import android.content.Context
import android.os.Environment
import com.spadilla89.proxyuniverse.data.model.Proxy
import com.spadilla89.proxyuniverse.data.model.ProxyProtocol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileExporter {
    
    /**
     * Export proxies to a text file
     * Returns the file path if successful, null otherwise
     */
    suspend fun exportProxies(
        context: Context,
        proxies: List<Proxy>,
        protocol: ProxyProtocol
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val timestamp = SimpleDateFormat(
                    Constants.EXPORT_DATE_FORMAT,
                    Locale.getDefault()
                ).format(Date())
                
                val fileName = "${Constants.EXPORT_FILE_PREFIX}${timestamp}${Constants.EXPORT_FILE_EXTENSION}"
                
                // Use the Downloads directory
                val downloadsDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )
                
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                
                val file = File(downloadsDir, fileName)
                
                // Generate file content
                val content = generateFileContent(proxies, protocol)
                
                // Write to file
                file.writeText(content)
                
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    
    /**
     * Generate the content for the export file
     */
    private fun generateFileContent(proxies: List<Proxy>, protocol: ProxyProtocol): String {
        val builder = StringBuilder()
        
        // Header
        builder.appendLine("# Universe Proxy Export")
        builder.appendLine("# Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}")
        builder.appendLine("# Protocol: ${protocol.displayName}")
        builder.appendLine("# Total: ${proxies.size} proxies")
        builder.appendLine()
        
        // Proxies
        proxies.forEach { proxy ->
            builder.appendLine(proxy.toFormattedString())
        }
        
        return builder.toString()
    }
    
    /**
     * Format proxies as a string for sharing or clipboard
     */
    fun formatProxiesAsString(proxies: List<Proxy>): String {
        return proxies.joinToString("\n") { it.toFormattedString() }
    }
    
    /**
     * Check if external storage is writable
     */
    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }
}
