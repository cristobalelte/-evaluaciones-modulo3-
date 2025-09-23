package com.example.ama.ui.screens.HomeScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.BottomBar

//Ruta = "subCategory"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSubCatScreen(
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_artemayor_horizontal),
                        contentDescription = "Arte Mayor"
                    )
                },
                // 👈 Botón de Configuración a la izquierda
                navigationIcon = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Configuración"
                        )
                    }
                },
                // 👉 Solo carrito a la derecha
                actions = {
                    IconButton(onClick = onOpenCart) {
                        BadgedBox(badge = { if (cartCount > 0) Badge { Text("$cartCount") } }) {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        },
        bottomBar = { BottomBar(onPublishClick = onOpenPublish) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("¿Qué artesanía buscas?") },
                singleLine = true,
                trailingIcon = { TextButton(onClick = { onSearch(query) }) { Text("Buscar") } }
            )

            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
//                Arrangment = Arrangement.Center,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                text = "Mantas"
            )
        }

        Spacer(
            Modifier.height(8.dp)
        )

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            Row 1 de 3 botones:
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        navController.navigate("productType")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text =  "Ordenar y.."
                    )
                }

                Button(
                    onClick = {
                        navController.navigate("productType")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)


                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "Region"
                    )
                }

                Button(
                    onClick = {
                        navController.navigate("productType")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "Precio"
                    )
                }


            } //Cierre Row 1

//            Row 2 de 2 botones:
            Row(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        navController.navigate("productType")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Text( textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "TAMAÑO"
                    )
                }

                Button(
                    onClick = {
                        navController.navigate("productType")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "COLOR"
                    )
                }

            } //Cierre Row 2
//        } //Cierre Col 2

//            Spacer(
//                Modifier.height(8.dp)
//            )


//          6 tarjetas de categorias del prod elegido:
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState())
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Row 3: 2 elementos:
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(4.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(
                    onClick = {
                        navController.navigate("guantes")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "Guantes"
                    )
                }



                Button(
                    onClick = {
                        navController.navigate("chalecos")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "Chalecos"
                    )
                }

            } //Cierre Row 1

//            Row 4 de 2 elementos:
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(4.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        navController.navigate("gorros")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Text( textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "Gorros"
                    )
                }


                Button(
                    onClick = {
                        navController.navigate("calcetines")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Text( textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "Calcetines"
                    )
                }

            } //Cierre Row 2

//            Row 5 de 2 elementos:
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(4.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        navController.navigate("ponchos")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Text( textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "Ponchos"
                    )
                }


                Button(
                    onClick = {
                        navController.navigate("mantas")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(1f)
                ) {
                    Text( textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "Mantas"
                    )
                }

            }

        }

    }
}

