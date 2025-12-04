package com.example.ama.ui.screens.products.publicados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.ama.data.dataclass.ProductData

@Composable
fun PublicadosCard(product: ProductData) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        var showPopup by remember { mutableStateOf(false) }
        var showPopup2 by remember { mutableStateOf(false) }

        // 3. Usa el componente Popup
        if (showPopup) {
            Popup(onDismissRequest = { showPopup = false }) { // Cierra la ventana al tocar fuera
                // Define el contenido de la ventana emergente aquí
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 8.dp),
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
                    )
                    {
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 8.dp),
                            text = "$${product.price}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    } //Cierre box

                    Text(
                        text = "${product.name}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )

                    Button(
                        onClick = {
                            showPopup = false
                            showPopup2 = true
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                    {
                        Text(
                            text = "Sí. Estoy seguro",
                            color = Color.White
                        )
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.Black
                        ),
                        onClick = { showPopup = false },
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                    {
                        Text(
                            text = "No. Cancelar"
                        )
                    }


                } //Cierre Column
            }
        }

        if (showPopup2) {
            Popup(onDismissRequest = { showPopup2 = false }) { // Cierra la ventana al tocar fuera
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 8.dp),
                        text = "Tu artesanía ${product.name} se ha eliminado correctamente",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )

                    Button(
                        onClick = { showPopup2 = false },
                        modifier = Modifier
                            .padding(end = 8.dp)
                    ){
                        Text(
                            text = "Volver",
                            color = Color.White
                        )
                    }

                }

            }
        }

        /*Image(
        painter = painterResource(id = R.drawable.logo_artemayor_horizontal),
        contentDescription = "Descripción de la imagen", // Es importante para la accesibilidad
        modifier = Modifier
            .fillMaxWidth()
            .height(194.dp), // Ajusta la altura según sea necesario
        contentScale = ContentScale.Crop // O usa ContentScale.Fit si prefieres
    )
    // Puedes agregar aquí otros elementos como texto, botones, etc.
    Text(
        text = "Título de la tarjeta",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(16.dp)
    )*/
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(Color.DarkGray)
            )
            {
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = "$${product.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
            Text(
                textAlign = TextAlign.Center,
                text = "${product.name}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Text(
                textAlign = TextAlign.Center,
                text = "${product.craftType}", //Categoria
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = { showPopup = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Eliminar")
                }

                Button(
                    onClick = { /* Acción al hacer clic en el botón */ },
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