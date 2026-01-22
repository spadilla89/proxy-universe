package com.spadilla89.proxyuniverse.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {
    
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }
    
    private val gson = GsonBuilder()
        .setLenient()
        .create()
    
    // ProxyScrape API
    val proxyScrapeApi: ProxyScrapeApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.proxyscrape.com/v2/")
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ProxyScrapeApi::class.java)
    }
    
    // GeoNode API
    val geoNodeApi: GeoNodeApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://proxylist.geonode.com/api/")
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GeoNodeApi::class.java)
    }
    
    // PubProxy API
    val pubProxyApi: PubProxyApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://pubproxy.com/api/")
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PubProxyApi::class.java)
    }
}
