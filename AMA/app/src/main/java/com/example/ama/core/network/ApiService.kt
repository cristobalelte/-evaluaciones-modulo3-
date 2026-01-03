package com.example.ama.core.network

import com.example.ama.core.dto.CategoryDto
import com.example.ama.core.dto.CreateProductRequest
import com.example.ama.core.dto.ProductDto
import com.example.ama.core.dto.ProductImageDto
import com.example.ama.core.dto.ProductsPageDto
import com.example.ama.core.dto.SellerProfileDto
import com.example.ama.core.dto.ShoppingCarsDto
import com.example.ama.data.dataclass.EquipoAmaItem
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api-json")
    suspend fun getSwaggerJson(): ResponseBody

    // core/network/ApiService.kt
    @POST("carrito")
    suspend fun upsertShoppingCar(@Body body: ShoppingCarsDto): retrofit2.Response<ShoppingCarsDto>
    @POST("productos")
    suspend fun createProduct(@Body body: CreateProductRequest): ProductDto
    @GET("carrito")
    suspend fun getShoppingCarList(
        @Query("owner") owner: String? = null
    ): List<ShoppingCarsDto>
    @GET("equipo/integrante/todo")
    suspend fun getEquipo(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): List<EquipoAmaItem>
    @POST("carrito")
    suspend fun upsertShoppingCarRaw(
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<Unit>    // o Response<Any> si prefieres
    @GET("productos/{id}")
    suspend fun getProduct(@Path("id") id: String): ProductDto

    @GET("productos/nuevo/todos")
    suspend fun getAllNewProducts(): List<ProductDto>

    @GET("productos")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("categoryId") categoryId: Int? = null,
        @Query("sellerUserId") sellerUserId: Int? = null,
        @Query("publicationStatus") publicationStatus: String? = null,
        @Query("search") search: String? = null,
        @Query("minPrice") minPrice: Int? = null,
        @Query("maxPrice") maxPrice: Int? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null
    ): ProductsPageDto
    @GET("products/categories")
    suspend fun getCategories(): List<CategoryDto>
    @GET("/productos/{productId}/imagenes")
    suspend fun getProductImages(
        @Path("productId") productId: Int
    ): List<ProductImageDto>
    @GET("/sellerProfiles")
    suspend fun getSellerProfiles(): List<SellerProfileDto>
}

