package com.spadilla89.proxyuniverse.data.api

import com.google.gson.annotations.SerializedName

// ProxyScrape API response models
data class ProxyScrapeResponse(
    @SerializedName("proxies")
    val proxies: List<ProxyScrapeProxy>?
)

data class ProxyScrapeProxy(
    @SerializedName("ip")
    val ip: String?,
    @SerializedName("port")
    val port: Int?,
    @SerializedName("protocol")
    val protocol: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("anonymity")
    val anonymity: String?
)

// GeoNode API response models
data class GeoNodeResponse(
    @SerializedName("data")
    val data: List<GeoNodeProxy>?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("limit")
    val limit: Int?
)

data class GeoNodeProxy(
    @SerializedName("ip")
    val ip: String?,
    @SerializedName("port")
    val port: String?,
    @SerializedName("protocols")
    val protocols: List<String>?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("anonymityLevel")
    val anonymityLevel: String?,
    @SerializedName("responseTime")
    val responseTime: Int?
)

// PubProxy API response models
data class PubProxyResponse(
    @SerializedName("data")
    val data: List<PubProxyData>?,
    @SerializedName("count")
    val count: Int?
)

data class PubProxyData(
    @SerializedName("ipPort")
    val ipPort: String?,
    @SerializedName("ip")
    val ip: String?,
    @SerializedName("port")
    val port: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("level")
    val level: String?,
    @SerializedName("speed")
    val speed: String?
)
