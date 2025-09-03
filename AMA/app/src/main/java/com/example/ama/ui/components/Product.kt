package com.example.ama.ui.components


import kotlinx.serialization.Serializable

@Serializable
enum class ProductType { TEXTIL, MADERA, CERAMICA, GREDA, HILO, PINTURA, OTRO }

// Product.kt

@Serializable
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String = "",
    val author: String,
    val isActive: Boolean,
    val stock: Int,
    val region: String,
    val type: ProductType
)
