package com.example.ama.ui.screens.products

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ama.core.dto.ProductDto

@Composable
fun ProductCard(product: ProductDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        product.name?.let {
            Text(
                text = "Nombre: $it",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.id?.let {
            Text(
                text = "Id: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.sellerUserId?.let {
            Text(
                text = "SellerUserId: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.description?.let {
            Text(
                text = "Descripción: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.categoryId?.let {
            Text(
                text = "CategoryId: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.material?.let {
            Text(
                text = "Material: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.color?.let {
            Text(
                text = "Color: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.size?.let {
            Text(
                text = "Tamaño: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.price?.let { price ->
            val currency = product.currency ?: ""
            Text(
                text = "Precio: $price $currency",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.stock?.let {
            Text(
                text = "Stock: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.publicationStatus?.let {
            Text(
                text = "Estado: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.publishedAt?.let {
            Text(
                text = "Publicado: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.createdAt?.let {
            Text(
                text = "Creado: $it",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
