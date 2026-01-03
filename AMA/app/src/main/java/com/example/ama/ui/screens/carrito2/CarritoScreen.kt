package com.example.ama.ui.screens.carrito2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.TopBar
import com.example.ama.ui.navigation.Routes
import com.example.ama.ui.screens.products.ProductList

//Ruta: carritoList
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit
){
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
                onHelpClick = {},
                onPublishClick = onOpenPublish,
                onProfileClick = { navController.navigate("profile") }
            )
        }
    )
    { innerPadding ->
        CarritoList(modifier = Modifier.padding(innerPadding))
    }
}