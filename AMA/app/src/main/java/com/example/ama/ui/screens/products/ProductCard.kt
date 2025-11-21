package com.example.ama.ui.screens.products

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ama.data.dataclass.ProductData

@Composable
fun ProductCard(product: ProductData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        product.name?.let {
            Text(
                text = "Nombre: $it", style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.creatorId?.let {
            Text(
                text = "creator_Id: $it", style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.craftType?.let {
            Text(
                text = "Categoria: $it", style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.createdAt?.let {
            Text(
                text = "Fecha: $it", style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }



        product.id?.let {
            Text(
                text = "Id: $it", style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.price?.let {
            Text(
                text = "Precio: $it", style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.region?.let {
            Text(
                text = "Region: $it", style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        product.material?: let {
            Text(
                text = "Material: $it", style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

        }

        product.stock?.let {
            Text(
                text ="Stock: $it", style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        product.isFeatured?.let {
            if (it) {
                Text(
                    text = "Producto destacado", style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Text(
                    text = "Producto no destacado", style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

        }

        product.isActive?.let {
            if (it) {
                Text(
                    text = "Producto activo", style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else {
                Text(
                    text = "Producto inactivo", style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

    }

}