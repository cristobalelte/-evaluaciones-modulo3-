package com.example.ama.ui.screens.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.data.dataclass.ProductData
import com.example.ama.data.viewmodel.ProductViewModel
import com.example.ama.data.viewmodel.ProductViewModelFactory
import com.example.ama.ui.Register.RegisterScreen
import com.example.ama.ui.Register.RegisterViewModel
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.navigation.Routes

//Ruta: perfil
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit,
//    para regScreen:
    registerVM: RegisterViewModel = viewModel(),
    registerScreen: Unit = RegisterScreen(
        registerVM = registerVM,
        navController = navController,
        onBack = { navController.popBackStack() }
    )

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
    //    Para ref cantidad de productos en el Backend, creamos var productList para tener la cant total con .size :
    val productListViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory()
    )
    val productList by productListViewModel.productList.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_artemayor_horizontal),
                        contentDescription = "Arte Mayor",
                        modifier = Modifier.clickable(onClick = { navController.navigate(Routes.HOME) })
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
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                if (registerVM.name != null){
                    "¡Hola ${registerVM.name}!"
                } else {
                    "¡Hola visitante!"
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            )

            Text(
                text = "Aqui puedes revisar tus avances y ganancias",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .weight(1f)

            ) {
                Box(
                    modifier = Modifier.clickable(onClick = { navController.navigate("prodPublicados") })
                        .fillMaxSize()
                        .weight(1f)
                        .background(color = Color.Gray),
                    contentAlignment = Alignment.Center,
                )
                {
                    Text(
                        text = "${productList.size} productos publicados",
                        color = Color.White
                    )
                }

                Spacer(
                    modifier = Modifier
                        .width(12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(color = Color.Gray),
                    contentAlignment = Alignment.Center
                )
                {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    )
                    {
                        //Agregar icono de escritura con lapiz
                        Icon(
                            imageVector = Icons.Outlined.Addchart,
                            tint = Color.White,
                            contentDescription = "Tu historia",
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                        )
                            Text(
                                text = "Cuenta tu historia",
                                color = Color.White,
                                modifier = Modifier
                                    .weight(1f)
                            )
                    }
                }

            }  //Cierre Row 1

            //Row 2:
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(color = Color.Gray),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        text = "2 Pedidos pendientes",
                        color = Color.White
                    )
                }

                Spacer(
                    modifier = Modifier
                        .width(12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(color = Color.Gray),
                    contentAlignment = Alignment.Center
                )
                {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            tint = Color.White,
                            contentDescription = "Datos",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        )
                        Text(
                            text = "Edita tus datos",
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        )
                    }
                }

            }
            //Cierre Row 2

            //Row 3:
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(color = Color.Gray),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        text = "1 Productos enviados",
                        color = Color.White
                    )
                }

                Spacer(
                    modifier = Modifier
                        .width(12.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(color = Color.Gray),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        text = "$55.000 Ganancias acumuladas",
                        color = Color.White
                    )
                }

            }
            //Cierre Row 3

            Button(
                onClick = { navController.navigate(Routes.LOGIN) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            {
                Text(text = "Salir del perfil")
            }
        }
        // Cierre Columna


    }
}