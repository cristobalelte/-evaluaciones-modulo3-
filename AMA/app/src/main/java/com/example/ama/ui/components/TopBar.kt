package com.example.ama.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.navigation.Routes
import com.example.ama.ui.theme.onPrimaryLight
import com.example.ama.ui.theme.primaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
) {
    CenterAlignedTopAppBar(
        expandedHeight = 100.dp,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = primaryLight, //Color de fondo
            titleContentColor = onPrimaryLight, //Color del texto
            navigationIconContentColor = onPrimaryLight, // Color del icono de navegación
            actionIconContentColor = onPrimaryLight
        ),
        navigationIcon = {
            Image(
                alignment = Alignment.Center,
                painter = painterResource(R.drawable.logo_artemayor_blanco),
                contentDescription = "Arte Mayor",
                modifier = Modifier
                    .padding(start = 18.dp)
                    .clickable(onClick = { navController.navigate(Routes.HOME) })
                    .width(120.dp),
                contentScale = ContentScale.Fit // O usa ContentScale.Fit si prefieres
            )
        },

        title = {
            /* Image(
                 alignment = Alignment.CenterStart,
                 painter = painterResource(R.drawable.logo_artemayor_blanco),
                 contentDescription = "Arte Mayor",
                 modifier = Modifier
                     .clickable(onClick = { navController.navigate(Routes.HOME) })
                     .width(100.dp),
                 contentScale = ContentScale.Fit // O usa ContentScale.Fit si prefieres
             )*/
        },
        /*     navigationIcon = {
                 IconButton(onClick = onOpenSettings) {
                     Icon(Icons.Filled.Settings, contentDescription = "Configuración")
                 }
             },*/
        actions = {
            IconButton(
                onClick = onOpenCart,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                BadgedBox(badge = { if (cartCount > 0) Badge { Text("$cartCount") } }) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                }
            }
        }
    )

}
