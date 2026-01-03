package com.example.ama.data.repository

import com.example.ama.core.network.ApiService
import com.example.ama.data.dataclass.ProductData
import com.example.ama.data.network.NetworkModule

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddProductRepository {
    private val api: ApiService = NetworkModule.apiService

    //    val products: Flow<List<ProductData>> = dao.getAll()
    private val limit = 3
    private val offset = 0

    // Funciones que solo interactúan con la API Service externa
//    Esta fun, recibe todos los productos pero desde la Nube
/*    fun addProduct(): Flow<List<ProductData>> = flow {
        val response = backEndApiService.addProduct(
            ProductData(
                id = 0,
                name = "Producto de prueba",
                description = "Descripción del producto de prueba",
                price = 100.0,
                material = "Material de prueba",
                craftType = "Tipo de crafteo de prueba",
                isFeatured = false,
                isActive = true,
                stock = 10,
                region = "Región de prueba",
                imageUrl = "URL de la imagen de prueba",
                author = "Autor de prueba",
                createdAt = "Fecha de creación de prueba",
                creatorId = "123"
            )

        )
    }*/
}

/*
*    ProductDataB(
                data = listOf(
                    Data(
                        categoryId = "1",
                        color = "rojo",
                        createdAt = "12/12/2022",
                        currency = "CLP",
                        description = "Producto de prueba",
                        id = "1",
                        material = "Madera",
                        name = "Producto de prueba",
                        price = "10000",
                        publicationStatus = "PUBLISHED",
                        publishedAt = "12/12/2022",
                        sellerUserId = "1",
                        size = "M",
                        stock = 10,
                        updatedAt = "12/12/2022",
                        tipo = com.example.ama.data.dataclass.ProductTipo.MADERA,
                        imageUrl = ""
                    )
                ),
                hasNextPage = false,
                hasPreviousPage = false,
                limit = 10,
                page = 1,
                total = 1,
                totalPages = 1
            )*/