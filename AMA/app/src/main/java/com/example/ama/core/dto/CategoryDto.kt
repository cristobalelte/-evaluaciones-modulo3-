package com.example.ama.core.dto

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("id") val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val parentId: String? = null,
    val parent: Any? = null,
    val children: List<Any> = emptyList(),
    val imageUrl: String? = null,
    val isActive: Boolean? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
data class HomeCategory(
    val id: String?,
    val name: String?,
    val imageUrl: String? = null
)
