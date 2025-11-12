package com.example.ama.core.network

import com.example.ama.core.dto.ShoppingCarsDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api-json")
    suspend fun getSwaggerJson(): ResponseBody

    // core/network/ApiService.kt
    @POST("carrito")
    suspend fun upsertShoppingCar(@Body body: ShoppingCarsDto): retrofit2.Response<ShoppingCarsDto>

    @GET("carrito")
    suspend fun getShoppingCarList(
        @Query("owner") owner: String? = null
    ): List<ShoppingCarsDto>
    @POST("carrito")
    suspend fun upsertShoppingCarRaw(
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<Unit>    // o Response<Any> si prefieres
}
