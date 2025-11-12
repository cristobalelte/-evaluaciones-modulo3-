// core/dto/ShoppingCarsDto.kt
package com.example.ama.core.dto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShoppingCarsDto(
    val id: Int? = null,
    val productos: List<ProductDto>,
    val total: Int,
    val owner: String,
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Int,
    val name: String,
    val price: Int,
    val material: String? = null,
    val craftType: String? = null,
    val creator: String? = null,
    val region: String? = null,
    val isFeatured: Boolean? = null,
    val createdAt: String? = null
)
