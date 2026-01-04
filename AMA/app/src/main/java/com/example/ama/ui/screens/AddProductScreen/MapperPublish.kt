package com.example.ama.ui.screens.AddProductScreen

import com.example.ama.core.dto.CreateProductRequest
import com.example.ama.core.dto.PublishForm

private fun PublishForm.toCreateRequestOrNull(): CreateProductRequest? {
    val nameOk = name.trim().takeIf { it.isNotBlank() } ?: return null
    val priceInt = price.trim().toIntOrNull() ?: return null
    if (priceInt <= 0) return null

    val stockInt = stock.trim().toIntOrNull() ?: 1

    val categoryInt = categoryId.trim()
        .takeIf { it.isNotBlank() }
        ?.toIntOrNull()

    return CreateProductRequest(
        name = nameOk,
        description = description.trim().takeIf { it.isNotBlank() },
        categoryId = categoryInt,
        material = material.trim().takeIf { it.isNotBlank() },
        color = color.trim().takeIf { it.isNotBlank() },
        size = size.trim().takeIf { it.isNotBlank() },
        price = priceInt,
        currency = currency.trim().ifBlank { "CLP" },
        stock = stockInt,
        publicationStatus = publicationStatus.trim().ifBlank { "PUBLISHED" }
    )
}