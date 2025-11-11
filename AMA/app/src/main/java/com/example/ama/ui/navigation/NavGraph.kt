package com.example.ama.ui.navigation

import android.app.Activity
import android.net.Uri
import com.example.ama.ui.screens.home.HomeScreen
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
import com.example.ama.ui.Login.LoginScreen
import com.example.ama.ui.Register.RegScreenDClass
import com.example.ama.ui.Register.RegisterScreen
import com.example.ama.ui.Register.RolScreen
import com.example.ama.ui.Register.StartScreen
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.screens.AddProductScreen.AddProductRoute

import com.example.ama.ui.screens.HomeScreen.ProductPay
import com.example.ama.ui.screens.HomeScreen.ProductRegionScreen
import com.example.ama.ui.screens.HomeScreen.ProductTypeScreen

import com.example.ama.ui.screens.carrito.CartRoute
import com.example.ama.ui.screens.carrito.DataEnvio
import com.example.ama.ui.screens.carrito2.CarritoScreen
import com.example.ama.ui.screens.catalog.CatalogRoute
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.checkout.DataEnvioScreen
import com.example.ama.ui.screens.detail.ProductDetailScreen
import com.example.ama.ui.screens.perfil.PerfilScreen
import com.example.ama.ui.screens.products.ProductScreen
import com.example.ama.ui.screens.settings.SettingsScreen
import com.example.ama.ui.screens.subcategory.ProductSubCatScreen
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


        composable(Routes.LOGIN) {
            LoginScreen(
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

        composable("rolScreen"){
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
                product = product,
                onBack = { navController.popBackStack() },
                onAddToCart = { vm.addToCart(it) },
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) }
            )
        }


        composable(Routes.CART) {
            CartRoute(vm = vm, onBack = { navController.popBackStack() })
        }

        // 👇 registra el destino de Datos de envío
        composable(Routes.DATOS_ENVIO) {
            DataEnvioScreen(
                navController = navController,
                cartCount = vm.cartCount,
                onBack = { navController.popBackStack() },
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { /* ... */ },
                onOpenSettings = { /* ... */ },
                onNext = { navController.navigate(Routes.METODO_PAGO) }
            )
        }


        composable(Routes.SETTINGS) {
            SettingsScreen(
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
            arguments = listOf(navArgument("category"){ type = NavType.StringType })
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

        composable("datosEnvio") {
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

        composable("carritoList") {
            CarritoScreen(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

    }
}

