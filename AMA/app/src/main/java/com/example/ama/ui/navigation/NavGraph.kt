package com.example.ama.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ama.ui.Login.LoginScreen
import com.example.ama.ui.screens.carrito.CartRoute
import com.example.ama.ui.screens.catalog.CatalogRoute
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.detail.ProductDetailRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val vm: CatalogViewModel = viewModel()

    // en AppNavigation()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate("catalog") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("catalog") {
            CatalogRoute(
                vm = vm,
                navController = navController,
                onViewDetail = { id -> navController.navigate("detail/$id") },
                onOpenCart = { navController.navigate("cart") }
            )
        }

        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            ProductDetailRoute(
                vm = vm,
                productId = id,
                onBack = { navController.popBackStack() }
            )
        }


        composable("cart") {
            CartRoute(
                onBack = { navController.popBackStack() },
                vm = vm
            )
        }
    }
}



