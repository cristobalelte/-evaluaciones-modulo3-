package com.example.ama.core.dto

data class ShoppingCarsDto(
    val id: Int,
    val productos: List<ProductDto>,
    val total: Double,
    val createdAt: String,
    val owner: String
)

data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val createdAt: String,
    val material: String,
    val craftType: String,
    val creator: String,
    val region: String,
    val isFeatured: Boolean
)

