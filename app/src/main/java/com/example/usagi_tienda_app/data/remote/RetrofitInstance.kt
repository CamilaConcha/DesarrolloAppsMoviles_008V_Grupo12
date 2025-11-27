package com.example.usagi_tienda_app.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.example.usagi_tienda_app.BuildConfig

/**
 * Instancia de Retrofit con interceptor de logging para depuración.
 */
object RetrofitInstance {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val client = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            addInterceptor(logging)
        }
    }.build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
