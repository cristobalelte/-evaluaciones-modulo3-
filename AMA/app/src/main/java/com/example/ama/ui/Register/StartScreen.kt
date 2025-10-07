package com.example.ama.ui.Register

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.navigation.Routes
import com.example.ama.ui.screens.home.ArtisanBanner
import com.example.ama.ui.screens.home.CategoryItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    navController: NavController
   /* cartCount: Int,
    onOpenCart: () -> Unit,
    onOpenSettings: () -> Unit,*/
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_artemayor_horizontal),
                        contentDescription = "Arte Mayor",
                        alignment = Alignment.Center
                    )
                }
                /*       navigationIcon = {
                           IconButton(onClick = onOpenSettings) {
                               Icon(Icons.Outlined.Settings, contentDescription = "Configuración")
                           }
                       },
                       actions = {
                           IconButton(onClick = onOpenCart) {
                               BadgedBox(badge = { if (cartCount > 0) Badge { Text("$cartCount") } }) {
                                   Icon(Icons.Outlined.ShoppingCart, contentDescription = "Carrito")
                               }
                           }
                       }*/
            )
        }
    )
    { padding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            Button(
                onClick = {
                    navController.navigate(Routes.LOGIN)
                    val text = "Abriendo Inicio de Sesion"
                    val duration: Int = Toast.LENGTH_SHORT
                    Toast.makeText(navController.context, text, duration).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B3F2C),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("INICIAR SESION")
            }

            Button(
                onClick = {
                    navController.navigate("registerScreen")
                    val text = "Abriendo Registro"
                    val duration: Int = Toast.LENGTH_SHORT
                    Toast.makeText(navController.context, text, duration).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B3F2C),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("REGISTRARSE")
            }

            Button(
                onClick = {
                    navController.navigate(Routes.HOME)
                    val text = "Abriendo Home"
                    val duration: Int = Toast.LENGTH_SHORT
                    Toast.makeText(navController.context, text, duration).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B3F2C),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("ENTRAR COMO INVITADO")
            }


        }


    }
}