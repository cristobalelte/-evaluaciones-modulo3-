package com.example.ama.ui.screens.HomeScreen


import android.widget.Toast
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.TopBar
import com.example.ama.ui.navigation.Routes


//Ruta = "productType"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductTypeScreen(
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
            TopBar(
                navController,
                cartCount,
                onOpenCart
            )
        },
        bottomBar = {
            BottomBar(
                onHelpClick = {},
                navController = navController,
                onPublishClick = onOpenPublish,
                onProfileClick = { navController.navigate("profile") }
            )
        }
    )
    { padding ->
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
//                text = ProductType.TEXTIL.toString()
                text = "Lana"
            )
        }

        Spacer(
            Modifier.height(8.dp)
        )

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(8.dp),
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
                        .padding(16.dp)
                ) {
                    Text("Ordenar y..")
                }

                Button(
                    onClick = {
                        navController.navigate("regionScreen")
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
                        .padding(16.dp)
                ) {
                    Text("Region")
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
                        .padding(16.dp)
                ) {
                    Text("Precio")
                }


            } //Cierre Row
        }

        Spacer(
            Modifier.height(8.dp)
        )


//            8 botones de categorias del prod elegido:
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    .padding(4.dp)
            ) {
                Text("Guantes")
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
                    .padding(4.dp)
            ) {
                Text("Chalecos")
            }


            Button(
                onClick = {
                    navController.navigate("gorros")
                    val text = "Abriendo Filtro"
                    val duration: Int = Toast.LENGTH_SHORT
                    Toast.makeText(navController.context, text, duration).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text("Gorros")
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
                    .padding(4.dp)
            ) {
                Text("Calcetines")
            }


            Button(
                onClick = {
                    navController.navigate("ponchos")
                    val text = "Abriendo Filtro"
                    val duration: Int = Toast.LENGTH_SHORT
                    Toast.makeText(navController.context, text, duration).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text("Ponchos")
            }


            Button(
                onClick = {
                    navController.navigate("subCategory")
                    val text = "Abriendo Filtro"
                    val duration: Int = Toast.LENGTH_SHORT
                    Toast.makeText(navController.context, text, duration).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text("Mantas")
            }


            Button(
                onClick = {
                    navController.navigate("amigurumis")
                    val text = "Abriendo Filtro"
                    val duration: Int = Toast.LENGTH_SHORT
                    Toast.makeText(navController.context, text, duration).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text("Amigurumis")
            }


            Button(
                onClick = {
                    navController.navigate("otros")
                    val text = "Abriendo Filtro"
                    val duration: Int = Toast.LENGTH_SHORT
                    Toast.makeText(navController.context, text, duration).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text("Otros")
            }


        }

    }
}
