package com.spadilla89.proxyuniverse.data.model

enum class AnonymityLevel(val displayName: String, val level: Int) {
    ELITE("Elite", 1),
    ANONYMOUS("Anonymous", 2),
    TRANSPARENT("Transparent", 3);

    companion object {
        fun fromString(value: String): AnonymityLevel? {
            return when (value.lowercase()) {
                "elite", "high anonymity", "high", "level 1" -> ELITE
                "anonymous", "medium", "level 2" -> ANONYMOUS
                "transparent", "low", "none", "level 3" -> TRANSPARENT
                else -> null
            }
        }
    }
}
