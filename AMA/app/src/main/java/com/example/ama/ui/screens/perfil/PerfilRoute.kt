package com.example.ama.ui.screens.perfil

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.screens.catalog.CatalogViewModel
import com.example.ama.ui.screens.home.ArtisanBanner
import com.example.ama.ui.screens.home.CategoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilRoute(
    vm: CatalogViewModel,
    navController: NavController,
    onViewDetail: (String) -> Unit,
    onOpenCart: () -> Unit,
    initialType: ProductType? = null,
    onBack: (() -> Unit)? = null

){
    /*
    val ctx = LocalContext.current

    // Carga catálogo + adjunta carrito una sola vez
    LaunchedEffect(Unit) {
        vm.loadFromDisk(ctx)
        vm.attachCart(ctx)
    }

    // State de VM
    val products      by vm.products.collectAsStateWithLifecycle()
    val cartCount     by vm.cartCount.collectAsStateWithLifecycle()
    val onlyAvail     by vm.onlyAvailable.collectAsStateWithLifecycle()
    val query         by vm.query.collectAsStateWithLifecycle()
    val selectedRegs  by vm.regions.collectAsStateWithLifecycle()
    val selectedTypes by vm.types.collectAsStateWithLifecycle()

    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Layout
    var isGrid by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    PerfilScreen(
        navController = NavController(ctx),
        cartCount = cartCount,
        onOpenCart = onOpenCart,
        onOpenPublish = { navController.navigate("publish") },
        onOpenSettings = { navController.navigate("settings") }
    )
    */

}
