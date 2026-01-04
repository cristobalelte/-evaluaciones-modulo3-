package com.example.ama.ui.screens.HomeScreen

import ArtisanBannerUi
import SellerProfilesViewModel
import SellerProfilesViewModelFactory
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.data.viewmodel.ProductViewModel
import com.example.ama.data.viewmodel.ProductViewModelFactory
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.Product
import com.example.ama.ui.screens.catalog.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onSearch: (String) -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit,


    onCategoryClickCategoryId: (Int) -> Unit,


    newThisMonth: List<Product> = emptyList(),
    @DrawableRes seasonalBannerRes: Int = R.drawable.logo_artemayor_horizontal,

    onOpenProduct: (Product) -> Unit = {},
    onSeeAllNew: () -> Unit = {},
) {
    val colors = MaterialTheme.colorScheme

    // ViewModel productos (tuyo)
    val productListViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory())
    val productList by productListViewModel.productList.collectAsState()

    // Catalog VM (tuyo) para "Lo nuevo de este mes"
    val catalogVm: CatalogViewModel = viewModel()
    val newProducts by catalogVm.newProducts.collectAsStateWithLifecycle()
    val isLoadingNew by catalogVm.isLoadingNew.collectAsStateWithLifecycle()
    val errorNew by catalogVm.errorNew.collectAsStateWithLifecycle()


    val homeVm: HomeViewModel = viewModel()
    val categories by homeVm.categories.collectAsState()
    val catLoading by homeVm.loadingCategories.collectAsState()
    val catError by homeVm.categoriesError.collectAsState()
//artesanos perfiles
    val sellersVm: SellerProfilesViewModel = viewModel(factory = SellerProfilesViewModelFactory())
    val sellerProfiles by sellersVm.profiles.collectAsState()
    val sellersLoading by sellersVm.loading.collectAsState()
    val sellersError by sellersVm.error.collectAsState()

    LaunchedEffect(Unit) {
        homeVm.loadCategories()
        catalogVm.loadNewProducts()
        sellersVm.loadSellerProfiles()
    }

    var query by remember { mutableStateOf("") }

    Scaffold(
        containerColor = colors.primary,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(colors.primary)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SafeImageRes(
                        resId = R.drawable.logo_artemayor_blanco,
                        contentDescription = "Arte Mayor",
                        modifier = Modifier.height(40.dp),
                        contentScale = ContentScale.Fit
                    )

                    IconButton(onClick = onOpenCart) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = colors.onPrimary
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                onPublishClick = onOpenPublish,
                onProfileClick = { navController.navigate("perfil") },
                onHelpClick = onOpenSettings,

            )
        }
    ) { padding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color.White
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // ---------------- BUSCADOR ----------------
                item {
                    val shape = RoundedCornerShape(24.dp)

                    BasicTextField(
                        value = query,
                        onValueChange = { query = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.onSurface),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(48.dp)
                            .clip(shape)
                            .background(Color.White)
                            .border(width = 1.dp, color = Color(0xFFBDBDBD), shape = shape)
                            .padding(horizontal = 12.dp), // <-- padding interno controlado
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF5F5F5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Search,
                                        contentDescription = null,
                                        tint = Color(0xFF757575),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(Modifier.width(10.dp))

                                Box(Modifier.weight(1f)) {
                                    if (query.isEmpty()) {
                                        Text(
                                            text = "¿Qué artesanía buscas?",
                                            color = Color(0xFF9E9E9E),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )
                }

                // ---------------- CATEGORÍAS (BACKEND) ----------------
                item {
                    when {
                        catLoading -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) { CircularProgressIndicator() }
                        }

                        catError != null -> {
                            Text(
                                text = catError ?: "Error",
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        else -> {
                            Column(modifier = Modifier.fillMaxWidth()) {

                                Text(
                                    text = "Categorías",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp, bottom = 10.dp)
                                )

                                CategoriesCarouselFigma(
                                    categories = categories,
                                    onCategoryClickCategoryId = { categoryId ->
                                        navController.navigate("subcategory/$categoryId")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }



                // ---------------- ARTESANOS ----------------
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFF3E8))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = "Conoce a nuestros artesanos y artesanas",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFDD7A33)
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            when {
                                sellersLoading -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) { CircularProgressIndicator() }
                                }

                                sellersError != null -> {
                                    Text(
                                        text = sellersError ?: "Error",
                                        color = MaterialTheme.colorScheme.error,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                else -> {
                                    val banners = sellerProfiles
                                        .take(10) // o .take(20)
                                        .map { sp ->
                                            ArtisanBannerUi(
                                                id = sp.userId?.toIntOrNull() ?: 0,
                                                title = sp.storeName?.takeIf { it.isNotBlank() } ?: "Artesano/a",
                                                subtitle = sp.artisanTitle?.takeIf { it.isNotBlank() }
                                                    ?: sp.commune?.takeIf { it.isNotBlank() }
                                                    ?: "",
                                                imageUrl = sp.imageUrl?.takeIf { it.isNotBlank() } // si viene null, queda placeholder
                                            )
                                        }

                                    ArtisanBannerRowRemote(
                                        banners = banners,
                                        onClick = { /* navController.navigate("artisan/${it.id}") */ }
                                    )
                                }
                            }
                        }
                    }
                }

                // ---------------- LO NUEVO DE ESTE MES ----------------
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Lo nuevo de este mes",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(Modifier.height(12.dp))

                        when {
                            isLoadingNew -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) { CircularProgressIndicator() }
                            }

                            errorNew != null -> {
                                Text(
                                    text = errorNew ?: "Error",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }

                            else -> {
                                if (newProducts.isEmpty()) {
                                    Text(
                                        text = "Aún no hay productos nuevos",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    val rows = newProducts.take(4).chunked(2)
                                    rows.forEach { rowItems ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            rowItems.forEach { p ->
                                                Box(Modifier.weight(1f)) {
                                                    NuevoMesCard(
                                                        product = p,
                                                        onFavClick = { /* TODO */ },
                                                        onClick = { /* TODO abrir detalle */ }
                                                    )
                                                }
                                            }
                                            if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                                        }
                                        Spacer(Modifier.height(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }


                // ---------------- ESPECIAL DE TEMPORADA ----------------
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Especial de temporada",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        SeasonalBanner(resId = seasonalBannerRes)
                    }
                }

                // ---------------- CATÁLOGO BACKEND ----------------
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        SectionTitle(
                            title = "Catálogo de productos",
                            trailing = {
                                TextButton(onClick = { navController.navigate("productList") }) {
                                    Text("Ver catálogo")
                                }
                            }
                        )
                    }
                }

                // ---------------- EQUIPO AMA ----------------
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        SectionTitle(
                            title = "Equipo de trabajo AMA",
                            trailing = {
                                TextButton(onClick = { navController.navigate("equipoScreen") }) {
                                    Text("Nuestro equipo")
                                }
                            }
                        )
                    }
                }

                // extra si viene
                if (newThisMonth.isNotEmpty()) {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            ProductRow(products = newThisMonth, onClick = onOpenProduct)
                        }
                    }
                }
            }
        }
    }
}







