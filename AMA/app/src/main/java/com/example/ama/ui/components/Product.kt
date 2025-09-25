package com.example.ama.ui.components


import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@Serializable
enum class ProductType { LANA, MADERA, CERAMICA, GREDA, HILO, PINTURA, OTRO }

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
val Product.priceFormatted: String
    get() = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        maximumFractionDigits = 0           // CLP sin decimales
        currency = Currency.getInstance("CLP")
    }.format(price)