package com.example.ama.core.dto.mappers

import com.example.ama.core.dto.ProductDto
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType

// DTO -> UI model
fun ProductDto.toUi(): Product = Product(
    id        = id.toString(),
    name      = name,
    price     = price.toDouble(),
    imageUrl  = "",                 // tu DTO no trae imagen; deja vacío o agrega campo si existe
    author    = creator ?: "",
    isActive  = true,
    stock     = 0,
    region    = region ?: "",
    type      = ProductType.OTRO,
    description = ""                // ajusta si el DTO trae descripción
)
