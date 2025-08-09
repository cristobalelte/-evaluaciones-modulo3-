package com.example.ama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ama.ui.screens.catalog.CatalogRoute
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.detail.ProductDetailRoute
import com.example.ama.ui.theme.AMATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AMATheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ÚNICA instancia compartida del ViewModel para todo el NavHost
    val vm: CatalogViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "catalog"
    ) {
        // Catálogo
        composable("catalog") {
            CatalogRoute(
                vm = vm,
                onViewDetail = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }

        // Detalle por ID
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            ProductDetailRoute(
                vm = vm, // misma instancia compartida
                productId = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
