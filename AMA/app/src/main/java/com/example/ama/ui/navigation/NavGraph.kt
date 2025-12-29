package com.example.ama.ui.navigation

import android.app.Activity
import android.net.Uri
import com.example.ama.ui.screens.HomeScreen.HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ama.data.dataclass.ProductData
import com.example.ama.ui.Login.LoginScreen
import com.example.ama.ui.Register.RegisterScreen
import com.example.ama.ui.Register.RolScreen
import com.example.ama.ui.Register.StartScreen
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.screens.AddProductScreen.AddProductRoute

import com.example.ama.ui.screens.HomeScreen.ProductPay
import com.example.ama.ui.screens.HomeScreen.ProductRegionScreen
import com.example.ama.ui.screens.HomeScreen.ProductTypeScreen

import com.example.ama.ui.screens.carrito.CartRoute
import com.example.ama.ui.screens.carrito.CartViewModel
import com.example.ama.ui.screens.carrito.DataEnvio
import com.example.ama.ui.screens.carrito2.CarritoScreen
import com.example.ama.ui.screens.catalog.CatalogRoute
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.HomeScreen.DataEnvioScreen
import com.example.ama.ui.screens.detail.ProductDetailScreen
import com.example.ama.ui.screens.equipo.EquipoScreen
import com.example.ama.ui.screens.perfil.PerfilScreen
import com.example.ama.ui.screens.products.ProductScreen
import com.example.ama.ui.screens.products.publicados.EditarProducto
import com.example.ama.ui.screens.products.publicados.ProdPublicados
import com.example.ama.ui.screens.settings.SettingsScreen
import com.example.ama.ui.screens.HomeScreen.ProductSubCatScreen
import com.example.ama.ui.theme.ThemeOption


@Composable
fun AppNavigation(
    skipLogin: Boolean = true,
    onChangeTheme: (ThemeOption) -> Unit = {},
    themeOpt: ThemeOption
) {
    val navController = rememberNavController()
    val ctx = LocalContext.current

    // Un único VM compartido para el grafo
    val vm: CatalogViewModel = viewModel()

    // Cargar catálogo (JSON) y adjuntar carrito (Room) al iniciar
    LaunchedEffect(Unit) {
        vm.loadFromDisk(ctx)
        vm.attachCart(ctx)
    }

    // Estado para el badge del carrito
    val cartCount by vm.cartCount.collectAsStateWithLifecycle(initialValue = 0)

    NavHost(
        navController = navController,
//        startDestination = if (skipLogin) Routes.HOME else Routes.LOGIN
        startDestination = "startScreen"
    )
    {
        composable("startScreen") {
            StartScreen(
                navController = navController
            )

        }

        composable(route = Routes.REGISTER) {
            val ctx = LocalContext.current
            val activity = ctx as? Activity
            RegisterScreen(
                navController = navController,
                onBack = {
                    val popped = navController.popBackStack()
                    if (!popped) activity?.finish()
                }
            )
        }
        composable(
            route = Routes.REGISTER_WITH_ROLE,
            arguments = listOf(navArgument("role") { defaultValue = "BUYER" })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "BUYER"
            RegisterScreen(
                navController = navController,
                initialRole = role
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                onLoggedIn = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }


        /*    composable("registerScreen") {
                RegScreenDClass(
                    navController = navController,
                    onPublish = { register ->
                        navController.navigate(Routes.HOME)
                    }
                )
            }*/

        composable("rolScreen") {
            RolScreen(
                navController = navController
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onSearch = { /* TODO */ },
                onCategoryClick = { type ->
                    navController.navigate("subcategory?category=${Uri.encode(type.name)}")
                },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                navController = navController //SOLO PARA BOTON DE PRUEBA
            )
        }

        composable(Routes.PUBLISH) {
            AddProductRoute(
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CATALOG) {
            CatalogRoute(
                vm = vm,
                navController = navController,
                onViewDetail = { id -> navController.navigate("detail/$id") },
                onOpenCart = { navController.navigate(Routes.CART) },
                initialType = null,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.CATALOG_ARG,
            arguments = listOf(navArgument("type") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val initialType = backStackEntry.arguments?.getString("type")
                ?.let { runCatching { ProductType.valueOf(it) }.getOrNull() }

            CatalogRoute(
                vm = vm,
                navController = navController,
                onViewDetail = { id -> navController.navigate("detail/$id") },
                onOpenCart = { navController.navigate(Routes.CART) },
                initialType = initialType,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            val product = vm.getById(id) ?: run { navController.popBackStack(); return@composable }
            val cartCount by vm.cartCount.collectAsState(initial = 0)

            ProductDetailScreen(
                navController = navController,
                product = product,
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onBack = { navController.popBackStack() },
//                Agregamos el producto al carrito:
                onAddToCart = {
                    vm.addToCart(product)
                },
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) }
            )
        }


        composable(route = Routes.EDITAR_PRODUCTO)
        {
            EditarProducto(
                navController = navController,
                product = ProductData(
                    name = "Producto de prueba",
                    description = "Descripción de prueba",
                    author = "Autor de prueba",
                    price = 100.0,
                    material = "Madera",
                    craftType = "Cerámica",
                    isFeatured = true,
                    isActive = true,
                    creatorId = "123",
                    stock = 10,
                    region = "Región de prueba",
                    createdAt = "2023-09-01T12:00:00Z",
                    imageUrl = "https://example.com/image.jpg",
                    id = null
                ),
                onBack = { navController.popBackStack() },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                description = null,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },

            )

        }







        composable(Routes.CART) {
            val cartVm: CartViewModel = viewModel()   // o hiltViewModel()
            CartRoute(
                navController = navController,
                vm = cartVm,
                onBack = { navController.popBackStack() },
                onCheckoutSuccess = { navController.navigate(Routes.DATOS_ENVIO) }
            )
        }

        composable("opcionEntrega") {
            DataEnvioScreen(
                cartCount = cartCount,
                navController = navController,
                count = vm.cartCount,
                onBack = { navController.popBackStack() },
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { /* ... */ },
                onOpenSettings = { /* ... */ },
                onNext = { navController.navigate(Routes.METODO_PAGO) }
            )
        }


        composable(Routes.SETTINGS) {
            SettingsScreen(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                themeOpt = themeOpt,
                onChangeTheme = onChangeTheme,
                onBack = { navController.popBackStack() }
            )
        }


//Composable nueva de prueba: ProductTypeScreen.kt
        composable("productType") {
            ProductTypeScreen(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                onSearch = { /* TODO */ }
            )
        }

        composable(
            route = Routes.SUBCATEGORY, // "subcategory?category={category}"
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStack ->
            val categoryStr = backStack.arguments?.getString("category") ?: ""
            val category = runCatching { ProductType.valueOf(categoryStr) }
                .getOrElse { ProductType.LANA } // fallback seguro

            ProductSubCatScreen(
                navController = navController,
                category = category,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },        // ✅ lambda en este scope
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },  // ✅ lambda en este scope
                onBack = { navController.popBackStack() },
                onSearch = { /* opcional: reenviar búsqueda */ }
            )
        }


        composable("regionScreen") {
            ProductRegionScreen(
                navController = navController,
                category = ProductType.LANA,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                onSearch = { /* TODO */ }
            )
        }

        composable(Routes.DATOS_ENVIO) {
            DataEnvio(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }


        composable("metodoPago") {
            ProductPay(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable("perfil") {
            PerfilScreen(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable("productList") {
            ProductScreen(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable("prodPublicados") {
            ProdPublicados(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )


        }

        composable("carritoList") {
            CarritoScreen(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }


        composable("equipoScreen") {
            EquipoScreen(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

    }
}

