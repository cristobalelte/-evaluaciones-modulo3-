package com.example.ama.core.dto

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class ShoppingCarsDto(
    val id: Int? = null,
    val productos: List<ProductDto> = emptyList(),
    val total: Int = 0,
    val owner: String = "",
    val createdAt: String? = null
)

data class ProductDto(
    val id: String? = null,

    @SerializedName("sellerUserId")
    val sellerUserId: String? = null,

    val name: String? = null,
    val description: String? = null,

    @SerializedName("categoryId")
    val categoryId: String? = null,

    val material: String? = null,
    val color: String? = null,
    val size: String? = null,

    // viene como "54000.00"
    val price: String? = null,
    val currency: String? = null,
    val stock: Int? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,

    @SerializedName("publicationStatus")
    val publicationStatus: String? = null,

    @SerializedName("publishedAt")
    val publishedAt: String? = null,

    val createdAt: String? = null,
    val updatedAt: String? = null
)
data class CreateProductRequest(
    val name: String,
    val description: String? = null,
    val categoryId: Int? = null,
    val material: String? = null,
    val color: String? = null,
    val size: String? = null,
    val price: Int,
    val currency: String? = "CLP",
    val stock: Int? = 1,
    val publicationStatus: String? = "PUBLISHED"
)
data class PublishForm(
    val name: String = "",
    val description: String = "",
    val categoryId: String = "",
    val material: String = "",
    val color: String = "",
    val size: String = "",
    val price: String = "",
    val currency: String = "CLP",
    val stock: String = "1",
    val publicationStatus: String = "PUBLISHED",
    val imageUri: Uri? = null
)

enum class PublishStep { STEP_1, STEP_2, STEP_3 }
//LOGIN
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("jwt")
    val token: String,
    val id: String? = null,
    val email: String? = null
)