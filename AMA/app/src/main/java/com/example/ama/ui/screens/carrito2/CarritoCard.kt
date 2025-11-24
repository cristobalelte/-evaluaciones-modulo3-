package com.example.ama.ui.screens.carrito2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ama.R
import com.example.ama.data.dataclass.ProductData

@Composable
fun CarritoCard(product: ProductData) {
   /* Card(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(Color.DarkGray)
            )
            {
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = "${product.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
            Text(
                textAlign = TextAlign.Center,
                text = "${product.name}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
                )
            Text(
                textAlign = TextAlign.Center,
                text = "${product.craftType}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
                )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = { *//* Acción al hacer clic en el botón *//* },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Eliminar")
                }

                Button(
                    onClick = { *//* Acción al hacer clic en el botón *//* },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Editar")
                }
            }

        }
    }*/
}

