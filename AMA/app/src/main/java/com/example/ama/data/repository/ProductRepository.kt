package com.example.ama.data.repository

import com.example.ama.core.dto.CategoryDto
import com.example.ama.core.dto.CreateProductRequest
import com.example.ama.core.dto.ProductDto
import com.example.ama.core.network.ApiService
import com.example.ama.data.network.NetworkModule
import com.example.ama.data.network.NetworkModule.apiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant

class ProductRepository(
    private val api: ApiService = NetworkModule.apiService

) {

    private val limit = 10
    private val offset = 0
    private val BASE_URL = "http://44.222.218.77:3000"
    /**
     * Antes: backEndApiService.getProducts(limit, offset) -> List<ProductData>
     * Ahora: ApiService.getProductsPage(page, limit) -> ProductsPageDto
     * Devuelve ProductDto para mantener coherencia con el resto del proyecto.
     */
    fun getProducts(
        page: Int = 1,
        limit: Int = 10,
        categoryId: Int? = null,
        sellerUserId: Int? = null,
        publicationStatus: String? = null,
        search: String? = null,
        sortBy: String? = null,
        sortOrder: String? = null
    ): Flow<List<ProductDto>> = flow {
        try {
            val res = api.getProducts(
                page = page,
                limit = limit,
                categoryId = categoryId,
                sellerUserId = sellerUserId,
                publicationStatus = publicationStatus,
                search = search,
                sortBy = sortBy,
                sortOrder = sortOrder
            )
            emit(res.data)
        } catch (_: HttpException) {
            emit(emptyList())
        } catch (_: IOException) {
            emit(emptyList())
        }
    }

    // Alias para no cambiar el resto del proyecto (lo dejo igual pero más robusto)
    suspend fun getAllNewProducts(): List<ProductDto> {
        return try {
            val res = api.getProducts(
                page = 1,
                limit = 4,
                publicationStatus = "PUBLISHED",
                sortBy = "publishedAt",
                sortOrder = "DESC"
            )
            res.data
        } catch (_: HttpException) {
            emptyList()
        } catch (_: IOException) {
            emptyList()
        }
    }

    // Si lo usas en algún lado, lo dejo (pero ya no es necesario si ordenas por string)
    private fun parseInstantOrEpoch(value: String?): Instant =
        try {
            if (value.isNullOrBlank()) Instant.EPOCH else Instant.parse(value)
        } catch (_: Exception) {
            Instant.EPOCH
        }
    suspend fun getPrimaryImageUrl(productId: Int): String? {
        return try {
            val imgs = api.getProductImages(productId)

            val chosen = imgs.firstOrNull { it.isPrimary == true }
                ?: imgs.minByOrNull { it.displayOrder ?: Int.MAX_VALUE }
                ?: imgs.firstOrNull()

            chosen?.imageUrl
        } catch (_: Exception) {
            null
        }
    }
    suspend fun createProduct(body: CreateProductRequest): ProductDto {
        return api.createProduct(body)
    }
    suspend fun getCategories(): List<CategoryDto> {
        return try {
            api.getCategories()
        } catch (_: Exception) {
            emptyList()
        }
    }
    suspend fun getAllCategories(): List<CategoryDto> =
        apiService.getCategories()
    fun fullImageUrl(path: String?): String? {
        val p = path?.trim().orEmpty()
        if (p.isBlank()) return null
        if (p.startsWith("http")) return p
        return BASE_URL.trimEnd('/') + "/" + p.trimStart('/')
    }
    suspend fun searchProducts(search: String?): List<ProductDto> {
        val safe = search?.trim().takeUnless { it.isNullOrBlank() }
        return try {
            val res = api.getProducts(
                page = 1,
                limit = 50,
                publicationStatus = "PUBLISHED",
                search = safe,
                sortBy = "publishedAt",
                sortOrder = "DESC"
            )
            res.data
        } catch (_: HttpException) {
            emptyList()
        } catch (_: IOException) {
            emptyList()
        }
    }
}
