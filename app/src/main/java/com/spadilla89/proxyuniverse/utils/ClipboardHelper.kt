package com.spadilla89.proxyuniverse.utils

import android.content.ClipData
import android.content.Context

object ClipboardHelper {
    
    /**
     * Copy text to clipboard
     */
    fun copyToClipboard(context: Context, text: String, label: String = "Universe Proxy") {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) 
            as android.content.ClipboardManager
        
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }
    
    /**
     * Get text from clipboard
     */
    fun getFromClipboard(context: Context): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) 
            as android.content.ClipboardManager
        
        return if (clipboard.hasPrimaryClip()) {
            val clip = clipboard.primaryClip
            if (clip != null && clip.itemCount > 0) {
                clip.getItemAt(0).text?.toString()
            } else {
                null
            }
        } else {
            null
        }
    }
}
