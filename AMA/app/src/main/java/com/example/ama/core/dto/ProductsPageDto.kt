
package com.example.ama.core.dto

import com.google.gson.annotations.SerializedName

data class ProductsPageDto(
    val data: List<ProductDto> = emptyList(),
    val total: Int? = null,
    val page: Int? = null,
    val limit: Int? = null,
    val totalPages: Int? = null,
    val hasNextPage: Boolean? = null,
    val hasPreviousPage: Boolean? = null
)
