package com.example.ama.core.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente OKHttp que fuerza Accept: application/json
    private val okHttpJsonClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val req = chain.request().newBuilder()
                .header("Accept", "application/json")
                .build()
            chain.proceed(req)
        }
        .build()

    // Moshi con soporte Kotlin
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://54.243.16.169:3000/") // luego usamos @Url absoluto
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpJsonClient)
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}
