package com.spadilla89.proxyuniverse.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProxyScrapeApi {
    @GET("?request=displayproxies&format=json")
    suspend fun getProxies(
        @Query("protocol") protocol: String,
        @Query("timeout") timeout: Int = 10000,
        @Query("country") country: String? = null,
        @Query("anonymity") anonymity: String? = null,
        @Query("ssl") ssl: String? = null
    ): Response<String>
}

interface GeoNodeApi {
    @GET("proxy-list")
    suspend fun getProxies(
        @Query("protocols") protocols: String,
        @Query("limit") limit: Int = 500,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "lastChecked",
        @Query("sort_type") sortType: String = "desc",
        @Query("country") country: String? = null,
        @Query("anonymityLevel") anonymityLevel: String? = null
    ): Response<GeoNodeResponse>
}

interface PubProxyApi {
    @GET("proxy")
    suspend fun getProxies(
        @Query("type") type: String,
        @Query("limit") limit: Int = 20,
        @Query("country") country: String? = null,
        @Query("level") level: String? = null,
        @Query("format") format: String = "json"
    ): Response<PubProxyResponse>
}
