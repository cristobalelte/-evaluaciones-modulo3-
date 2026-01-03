package com.example.ama.ui.screens.products.publicados

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.data.viewmodel.ProductViewModel
import com.example.ama.data.viewmodel.ProductViewModelFactory
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdPublicados(
    modifier: Modifier = Modifier,
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val productListViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory()
    )

    val productList by productListViewModel.productList.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = onOpenCart
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
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.padding(paddingValues),
            state = listState
        ) {

            items(productList) { product ->
                PublicadosCard(
                    navController = navController,
                    product = product
                )
            }
        }
    }
}
