package com.example.ama.data.services

import com.example.ama.data.dataclass.EquipoAmaItem
import com.example.ama.data.dataclass.ProductData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.getValue

//Este archivo se usa con el ProductData.kt
interface BackEndApiService {
    object RetrofitInstance {
        //        private const val BASE_URL = "http://3.128.184.226:3000/"
        //Ultima version: http://44.222.218.77:3000/api#/
        private const val BASE_URL = "http://44.222.218.77:3000/"
        val api: BackEndApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BackEndApiService::class.java)
        }
    }

    //    @GET("productos?maxPrice=50000&minPrice=1000&sortByPrice=true&creatorId=42")
    @GET("productos")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<ProductData>

    //    @GET("carrito")
    @GET("carrito")
    suspend fun getCarrito(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<ProductData>


    // URL para get equipo: http://3.128.184.226:3000/equipo/integrante/todo
    @GET("equipo/integrante/todo")
    suspend fun getEquipo(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<EquipoAmaItem>


    /*    @GET("meds_list/{id}.json")
        suspend fun getMedicamentoById(@Path("id") id: Int): ProductData?

        @POST("meds_list.json")
        suspend fun addMedicamento(@Body contact: ProductData): ProductData

        @PUT("meds_list/{id}.json")
        suspend fun updateMedicamento(@Path("id") id: Int, @Body contact: ProductData): ProductData

        @DELETE("meds_list/{id}.json")
        suspend fun deleteMedicamento(@Path("id") id: Int)*/
}