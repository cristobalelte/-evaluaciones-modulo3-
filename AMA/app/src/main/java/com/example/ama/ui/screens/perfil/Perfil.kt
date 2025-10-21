package com.example.ama.ui.screens.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Addchart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.ProductType

//Ruta: perfil
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
  //  onSearch: (String) -> Unit,
   // onCategoryClick: (ProductType) -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit,

    // NUEVO: data para las secciones
    /*categories: List<CategoryItem> = defaultCategories(),
    artisanBanners: List<ArtisanBanner> = sampleArtisanBanners(),
    newThisMonth: List<Product> = emptyList(),
    @DrawableRes seasonalBannerRes: Int = R.drawable.logo_artemayor_horizontal,
    onOpenArtisan: (ArtisanBanner) -> Unit = {},
    onOpenProduct: (Product) -> Unit = {},
    onSeeAllNew: () -> Unit = {},
     */
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_artemayor_horizontal),
                        contentDescription = "Arte Mayor"
                    )
                },
                navigationIcon = {
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
                }
            )
        },
        bottomBar = {
            BottomBar(
                onPublishClick = onOpenPublish,
                onProfileClick = { navController.navigate("perfil") }
            )
        }
    ) //Cierre Scaffold

    { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(
                text = "¡Hola Fernando!",
                modifier = Modifier
                    .padding(16.dp)
                    .size(12.dp)
            )

            Text(
                text = "Aqui puedes revisar tus avances y ganancias",
                modifier = Modifier
                    .padding(16.dp)
                    .size(8.dp)
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .size(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(16.dp)
                        .background(color = Color.DarkGray)
                )
                {
                    Text(
                        text = "3 Productos publicados",
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(16.dp)
                        .background(color = Color.DarkGray)
                )
                {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(16.dp)
                    )
                    {
                        //Agregar icono de escritura con lapiz
                        Icon(
                            imageVector = Icons.Outlined.Addchart,
                            contentDescription = "Cuenta tu historia",
                        )
                        Text(
                            text = "Cuenta tu historia",
                            color = Color.White
                        )
                    }
                }

            }  //Cierre Row 1

            //Row 2:
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .size(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(16.dp)
                        .background(color = Color.DarkGray)
                )
                {
                    Text(
                        text = "2 Pedidos pendientes",
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(16.dp)
                        .background(color = Color.DarkGray)
                )
                {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Edita tus datos"
                        )
                        Text(
                            text = "Edita tus datos",
                            color = Color.White
                        )
                    }
                }

            }
            //Cierre Row 2

            //Row 3:
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .size(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(16.dp)
                        .background(color = Color.DarkGray)
                )
                {
                    Text(
                        text = "1 Productos enviados",
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(16.dp)
                        .background(color = Color.DarkGray)
                )
                {
                    Text(
                        text = "$55.000 Ganancias acumuladas",
                        color = Color.White
                    )
                }

            }
            //Cierre Row 3

            Button(onClick = { /*TODO*/ })
            {
                Text(text = "Salir del perfil")
            }
        }
        // Cierre Columna


    }
}