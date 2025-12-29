package com.example.ama.core.network

import com.example.ama.data.network.AuthApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {


    // Si tu backend es http://3.128.184.226:3000/api entonces:
    private const val BASE_URL = "http://3.128.184.226:3000/api/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val api: ApiService = retrofit.create(ApiService::class.java)
    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
}
