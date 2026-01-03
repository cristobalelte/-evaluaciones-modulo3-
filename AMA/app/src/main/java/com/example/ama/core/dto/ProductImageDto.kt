// core/dto/ProductImageDto.kt
package com.example.ama.core.dto

import com.google.gson.annotations.SerializedName
data class ProductImageDto(
    val id: Int? = null,

    @SerializedName("productId")
    val productId: Int? = null,

    @SerializedName("imageUrl")
    val imageUrl: String? = null,

    @SerializedName("altText")
    val altText: String? = null,

    @SerializedName("displayOrder")
    val displayOrder: Int? = null,

    @SerializedName("isPrimary")
    val isPrimary: Boolean? = null,

    val createdAt: String? = null,
    val updatedAt: String? = null
)