package com.example.ama.ui.screens.products

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ama.data.ProductData

@Composable
fun ProductCard(product: ProductData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = product.name, style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )


    }

}