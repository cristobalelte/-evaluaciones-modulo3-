package com.example.ama.core.dto

data class ShoppingCarsDto(
    val id: Int? = null,
    val productos: List<ProductDto> = emptyList(),
    val total: Int = 0,
    val owner: String = "",
    val createdAt: String? = null
)

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
