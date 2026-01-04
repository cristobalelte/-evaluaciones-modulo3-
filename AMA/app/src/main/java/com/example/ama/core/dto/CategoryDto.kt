package com.example.ama.core.dto

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("parentId") val parentId: String? = null,

    // si el backend lo trae, mejor tiparlo:
    @SerializedName("parent") val parent: CategoryDto? = null,

    // CLAVE: tipar children
    @SerializedName("children") val children: List<CategoryDto> = emptyList(),

    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("isActive") val isActive: Boolean? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class HomeCategory(
    val id: String?,
    val name: String?,
    val imageUrl: String? = null
)
