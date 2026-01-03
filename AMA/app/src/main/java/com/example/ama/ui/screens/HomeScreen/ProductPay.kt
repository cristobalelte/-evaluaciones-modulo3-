package com.example.ama.ui.screens.HomeScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.TopBar

//metodoPago
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductPay(
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                navController,
                cartCount,
                onOpenCart
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                onPublishClick = onOpenPublish,
                onHelpClick = onOpenSettings,
                onProfileClick = { navController.navigate("perfil") })
        }
    )
    { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
//                Arrangment = Arrangement.Center,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    text = "Metodo de Pago"
                )

            }

            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
//                Arrangment = Arrangement.Center,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    text = "Tarjetas guardadas"
                )

            }

            item {
                Button(
                    onClick = {
                        navController.navigate("metodoPago")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
//                    enabled = items.isNotEmpty(),
                    //Cambio de shape btn proceder al pago:
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "TARJETA DE DEBITO"
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        navController.navigate("metodoPago")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
//                    enabled = items.isNotEmpty(),
                    //Cambio de shape btn proceder al pago:
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "TARJETA DE CREDITO"
                    )
                }
            }

            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
//                Arrangment = Arrangement.Center,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    text = "Tarjeta nueva"
                )

            }

//            2 botones:

            item {
                Button(
                    onClick = {
                        navController.navigate("metodoPago")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
//                    enabled = items.isNotEmpty(),
                    //Cambio de shape btn proceder al pago:
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "TARJETA DE DEBITO"
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        navController.navigate("metodoPago")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
//                    enabled = items.isNotEmpty(),
                    //Cambio de shape btn proceder al pago:
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "TARJETA DE CREDITO"
                    )
                }
            }

            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
//                Arrangment = Arrangement.Center,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    text = "Otros métodos de pago"
                )

            }

//            2 botones:

            item {
                Button(
                    onClick = {
                        navController.navigate("metodoPago")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
//                    enabled = items.isNotEmpty(),
                    //Cambio de shape btn proceder al pago:
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "WEBPAY"
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        navController.navigate("metodoPago")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
//                    enabled = items.isNotEmpty(),
                    //Cambio de shape btn proceder al pago:
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "MERCADO PAGO"
                    )
                }
            }


        }
    }
}