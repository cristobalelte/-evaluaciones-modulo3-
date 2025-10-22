package com.example.ama.ui.screens.carrito

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.BottomBar

//Ruta = "datosEnvio"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataEnvio(
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit
) {
//    var query by remember { mutableStateOf("") }
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
        bottomBar = { BottomBar(onPublishClick = onOpenPublish,
            onProfileClick = { navController.navigate("perfil") }) }
        
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /*OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("¿Qué artesanía buscas?") },
                singleLine = true,
                trailingIcon = { TextButton(onClick = { onSearch(query) }) { Text("Buscar") } }
            )*/

            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
//                Arrangment = Arrangement.Center,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                text = "Informacion de despacho"
            )

            Spacer(
                Modifier.height(4.dp)
            )

            TextField(
                value = "",
                onValueChange = { },
                label = { Text("NOMBRE Y APELLIDO") },
                placeholder = { Text("NOMBRE Y APELLIDO") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )

            TextField(
                value = "",
                onValueChange = { },
                label = { Text("CALLE Y NÚMERO") },
                placeholder = { Text("CALLE Y NÚMERO") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )

            TextField(
                value = "",
                onValueChange = { },
                label = { Text("REGION") },
                placeholder = { Text("REGION") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )

            TextField(
                value = "",
                onValueChange = { },
                label = { Text("COMUNA") },
                placeholder = { Text("COMUNA") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )

            TextField(
                value = "",
                onValueChange = { },
                label = { Text("NUMERO DE TELEFONO") },
                placeholder = { Text("NUMERO DE TELEFONO") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )

            Button(
                onClick = {
                    navController.navigate("opcionEntrega")
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
                    text = "GUARDAR Y CONTINUAR"
                )
            }




        } //Cierre >Col





    }
}