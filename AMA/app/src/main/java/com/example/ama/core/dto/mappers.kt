package com.example.ama.core.mappers

import com.example.ama.core.dto.CreateProductRequest
import com.example.ama.core.dto.ProductDto
import com.example.ama.core.dto.PublishForm
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType


// DTO -> UI model
private fun ProductDto.toUi(): Product = Product(
    id = (id ?: 0).toString(),
    name = name ?: "(Sin nombre)",
    price = (price?.toDoubleOrNull() ?: 0.0),
    imageUrl = "",
    author = sellerUserId?.toString() ?: "",
    isActive = publicationStatus == "PUBLISHED",
    stock = stock ?: 0,
    region = "",
    type = ProductType.OTRO,
    description = description ?: "",
    createdAt = 0L
)
