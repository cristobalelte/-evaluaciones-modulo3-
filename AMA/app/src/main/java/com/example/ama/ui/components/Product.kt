package com.example.ama.ui.components


enum class ProductType { TEXTIL, MADERA, CERAMICA, GREDA, HILO, PINTURA, OTRO }

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val author: String,
    val isActive: Boolean = true,
    val stock: Int = 1,
    val region: String,
    val type: ProductType
)
