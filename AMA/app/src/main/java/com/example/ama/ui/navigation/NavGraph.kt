package com.example.ama.ui.navigation

import com.example.ama.ui.screens.home.HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ama.ui.Login.LoginScreen
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.screens.AddProductScreen.AddProductRoute

import com.example.ama.ui.screens.carrito.CartRoute
import com.example.ama.ui.screens.catalog.CatalogRoute
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.detail.ProductDetailRoute


@Composable
fun AppNavigation(skipLogin: Boolean = true) {
    val navController = rememberNavController()
    val vm: CatalogViewModel = viewModel()

    // 👇 colecta los StateFlow que necesites como valores
    val cartCount by vm.cartCount.collectAsState(initial = 0)

    NavHost(
        navController = navController,
        startDestination = if (skipLogin) Routes.HOME else Routes.LOGIN
    ) {
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

        composable(Routes.HOME) {
            HomeScreen(
                cartCount = cartCount,
                onOpenCart = { navController.navigate(Routes.CART) },
                onSearch = { /* ... */ },
                onCategoryClick = { type -> navController.navigate("catalog?type=${type.name}") },
                onOpenPublish = { navController.navigate(Routes.PUBLISH) }   // 👈 aquí navegas
            )
        }

        composable(Routes.PUBLISH) {
            AddProductRoute(
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
                onBack       = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.CATALOG_ARG,
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val initialType = backStackEntry.arguments?.getString("type")
                ?.let { runCatching { ProductType.valueOf(it) }.getOrNull() }

            CatalogRoute(
                vm = vm,
                navController = navController,
                onViewDetail = { id -> navController.navigate("detail/$id") },
                onOpenCart   = { navController.navigate("cart") },
                initialType  = initialType,
                onBack       = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            ProductDetailRoute(
                vm = vm,
                productId = id,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CART) {
            CartRoute(
                onBack = { navController.popBackStack() },
                vm = vm
            )
        }
    }
}
