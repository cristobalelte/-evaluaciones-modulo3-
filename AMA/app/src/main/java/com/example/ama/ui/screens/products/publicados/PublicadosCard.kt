package com.example.ama.ui.screens.products.publicados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.ama.core.dto.ProductDto
import com.example.ama.ui.navigation.Routes

@Composable
fun PublicadosCard(
    navController: NavController,
    product: ProductDto
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        var showPopup by remember { mutableStateOf(false) }
        var showPopup2 by remember { mutableStateOf(false) }

        val priceText = "${product.price ?: 0} ${product.currency ?: ""}".trim()
        val nameText = product.name ?: "(Sin nombre)"
        val categoryText = product.categoryId?.let { "Categoría: $it" } ?: "Categoría: -"

        // ---------------- POPUP 1 (Confirmación) ----------------
        if (showPopup) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { showPopup = false }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp),
                            text = "¿Estás seguro de que deseas eliminar tu artesanía?",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .background(Color.DarkGray)
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp),
                                text = "$$priceText",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }

                        Text(
                            text = nameText,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )

                        Button(
                            onClick = {
                                showPopup = false
                                showPopup2 = true
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = "Sí. Estoy seguro", color = Color.White)
                        }

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.LightGray,
                                contentColor = Color.Black
                            ),
                            onClick = { showPopup = false },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = "No. Cancelar")
                        }
                    }
                }
            }
        }

        // ---------------- POPUP 2 (Confirmación eliminado) ----------------
        if (showPopup2) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { showPopup2 = false }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp),
                            text = "Tu artesanía $nameText se ha eliminado correctamente",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )

                        Button(
                            onClick = { showPopup2 = false },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = "Volver", color = Color.White)
                        }
                    }
                }
            }
        }

        // ---------------- CONTENIDO CARD ----------------
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(Color.DarkGray)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp),
                    text = "$$priceText",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Text(
                textAlign = TextAlign.Center,
                text = nameText,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )


            Text(
                textAlign = TextAlign.Center,
                text = categoryText,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    onClick = { showPopup = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Eliminar")
                }

                Button(
                    onClick = {
                        navController.navigate(Routes.EDITAR_PRODUCTO)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Editar")
                }
            }
        }
    }
}
