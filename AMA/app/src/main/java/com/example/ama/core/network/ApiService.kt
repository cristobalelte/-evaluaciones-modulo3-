package com.example.ama.core.network

import com.example.ama.core.dto.ShoppingCarsDto


import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    // Swagger (OpenAPI JSON)
    @GET
    suspend fun getSwaggerJson(@Url absoluteUrl: String): ResponseBody

    // Carrito: la API devuelve LISTA de shoppingCarsDTO en GET /carrito
    @Headers("Accept: application/json")
    @GET
    suspend fun getShoppingCarList(@Url url: String): List<ShoppingCarsDto>

    // Fallback por si algún día devuelven un objeto en vez de lista
    @Headers("Accept: application/json")
    @GET
    suspend fun getShoppingCarObject(@Url absoluteUrl: String): ShoppingCarsDto

    // Agrega esto:
    @POST("carrito")
    suspend fun upsertShoppingCar(@Body body: ShoppingCarsDto): ShoppingCarsDto

}

