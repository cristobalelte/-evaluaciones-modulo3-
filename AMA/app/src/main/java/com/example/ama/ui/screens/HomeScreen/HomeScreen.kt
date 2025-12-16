package com.example.ama.ui.screens.HomeScreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.data.viewmodel.ProductViewModel
import com.example.ama.data.viewmodel.ProductViewModelFactory
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.priceFormatted
import com.example.ama.ui.navigation.Routes
import androidx.compose.foundation.ExperimentalFoundationApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onSearch: (String) -> Unit,
    onCategoryClick: (ProductType) -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit,
    categories: List<CategoryItem> = defaultCategories(),
    artisanBanners: List<ArtisanBanner> = sampleArtisanBanners(),
    newThisMonth: List<Product> = emptyList(),
    @DrawableRes seasonalBannerRes: Int = R.drawable.logo_artemayor_horizontal,
    onOpenArtisan: (ArtisanBanner) -> Unit = {},
    onOpenProduct: (Product) -> Unit = {},
    onSeeAllNew: () -> Unit = {},
) {
    val productListViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory())
    val productList by productListViewModel.productList.collectAsState()

    var query by remember { mutableStateOf("") }
    val colors = MaterialTheme.colorScheme

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
                    Image(
                        painter = painterResource(R.drawable.logo_artemayor_blanco),
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
                onProfileClick = { navController.navigate("perfil") }
            )
        }
    ) { padding ->
        val topPadding = padding.calculateTopPadding()

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color.White
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 🔍 BUSCADOR
                item {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(48.dp),
                        singleLine = true,
                        placeholder = { Text("¿Qué artesanía buscas?") },
                        leadingIcon = {
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
                                    tint = Color(0xFF757575)
                                )
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedBorderColor = Color(0xFFBDBDBD),
                            unfocusedBorderColor = Color(0xFFBDBDBD),
                            disabledBorderColor = Color(0xFFBDBDBD),
                            focusedTextColor = colors.onSurface,
                            unfocusedTextColor = colors.onSurface,
                            focusedPlaceholderColor = Color(0xFF9E9E9E),
                            unfocusedPlaceholderColor = Color(0xFF9E9E9E),
                            focusedLeadingIconColor = Color(0xFF757575),
                            unfocusedLeadingIconColor = Color(0xFF757575)
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSearch(query) })
                    )
                }

                // TÍTULO CATEGORÍAS CENTRADO
                item {
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 4.dp),
                        textAlign = TextAlign.Center
                    )
                }

                // CARRUSEL CATEGORÍAS
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        CategoryCarousel(
                            items = categories,
                            onClick = { onCategoryClick(it.type) },
                        )
                    }
                }

                // BLOQUE DESTACADO: CONOCE A NUESTROS ARTESANOS
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFF3E8)) // cremita
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

                            ArtisanBannerRow(
                                banners = artisanBanners,
                                onClick = onOpenArtisan
                            )
                        }
                    }
                }


                item { Spacer(Modifier.height(12.dp)) }

                // LO NUEVO DE ESTE MES
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Lo nuevo de este mes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 8.dp)
                        )

                        val productsToShow = productList.take(4)
                        val rows = productsToShow.chunked(2)

                        rows.forEach { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { product ->
                                    Box(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        NuevoMesCard(product = product)
                                    }
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                // ESPECIAL DE TEMPORADA
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

                // CARRITO
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        SectionTitle(
                            title = "Carrito de compras",
                            trailing = {
                                TextButton(
                                    onClick = { navController.navigate("carritoList") }
                                ) {
                                    Text("Ver carrito de backend")
                                }
                            }
                        )
                    }
                }

                // EQUIPO AMA
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        SectionTitle(
                            title = "Equipo de trabajo AMA",
                            trailing = {
                                TextButton(
                                    onClick = { navController.navigate("equipoScreen") }
                                ) {
                                    Text("Nuestro equipo")
                                }
                            }
                        )
                    }
                }

                // Fila extra de productos (si quieres usarla)
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        ProductRow(
                            products = newThisMonth,
                            onClick = onOpenProduct
                        )
                    }
                }
            }
        }
    }
}

/* --------------------------- DATA CLASSES --------------------------- */

data class CategoryItem(
    val label: String,
    @DrawableRes val iconRes: Int,
    val type: ProductType
)

data class ArtisanBanner(
    val id: String,
    val title: String,
    @DrawableRes val imageRes: Int
)

/* --------------------------- UI HELPERS --------------------------- */

@Composable
private fun SectionTitle(
    title: String,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        trailing?.invoke()
    }
}

@Composable
fun CroppedCategoryImage(
    @DrawableRes resId: Int,
    boxSize: Dp = 44.dp,
    bottomCutFraction: Float = 0.65f,
    maskBottomFraction: Float = 0.18f
) {
    Box(
        modifier = Modifier
            .size(boxSize)
            .clip(CircleShape)
            .drawWithContent {
                val cut = this.size.height * bottomCutFraction
                clipRect(0f, 0f, this.size.width, this.size.height - cut) {
                    this@drawWithContent.drawContent()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(resId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(boxSize * maskBottomFraction)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}

/* --------------------------- CATEGORÍAS --------------------------- */

@Composable
fun CategoryCarousel(
    items: List<CategoryItem>,
    onClick: (CategoryItem) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items) { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillParentMaxWidth(1f / 3f)
                    .padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = CircleShape
                        )
                        .clickable { onClick(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(item.iconRes),
                        contentDescription = item.label,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ArtisanBannerRow(
    banners: List<ArtisanBanner>,
    onClick: (ArtisanBanner) -> Unit
) {
    val state = rememberLazyListState()
    val fling = rememberSnapFlingBehavior(state)

    LazyRow(
        state = state,
        flingBehavior = fling,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(banners) { b ->
            Card(
                onClick = { onClick(b) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(260.dp)
                    .height(120.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(b.imageRes),
                        contentDescription = b.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0x66000000))
                                )
                            )
                    )
                    Text(
                        b.title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductRow(
    products: List<Product>,
    onClick: (Product) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(products) { p ->
            ProductCardSmall(product = p, onClick = { onClick(p) })
        }
    }
}

@Composable
private fun ProductCardSmall(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(170.dp)
            .height(220.dp)
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = product.priceFormatted,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = product.author,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SeasonalBanner(@DrawableRes resId: Int) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Image(
            painter = painterResource(resId),
            contentDescription = "Especial de temporada",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/* --------------------------- DATA DE MUESTRA --------------------------- */

private fun defaultCategories() = listOf(
    CategoryItem("Lana", R.drawable.lana_icon, ProductType.LANA),
    CategoryItem("Madera", R.drawable.madera_icon, ProductType.MADERA),
    CategoryItem("Cerámica", R.drawable.ceramica_icon, ProductType.CERAMICA),
    CategoryItem("Greda", R.drawable.greda_icon, ProductType.GREDA),
    CategoryItem("Hilo", R.drawable.hilo_icon, ProductType.HILO),
    CategoryItem("Pintura", R.drawable.pintura_icon, ProductType.PINTURA),
)

private fun sampleArtisanBanners() = listOf(
    ArtisanBanner(
        id = "a1",
        title = ImagenesEnumeration.ArtesanosDelSur.nombre,
        imageRes = ImagenesEnumeration.ArtesanosDelSur.imgLoc
    ),
    ArtisanBanner(
        id = "a2",
        title = ImagenesEnumeration.TejedorasDeChiloe.nombre,
        imageRes = ImagenesEnumeration.TejedorasDeChiloe.imgLoc
    ),
    ArtisanBanner(
        id = "a3",
        title = ImagenesEnumeration.MadererosDelMaule.nombre,
        imageRes = ImagenesEnumeration.MadererosDelMaule.imgLoc
    ),
)
