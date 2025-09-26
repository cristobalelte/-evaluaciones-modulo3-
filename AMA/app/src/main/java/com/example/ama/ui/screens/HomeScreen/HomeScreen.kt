package com.example.ama.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.priceFormatted
import com.example.ama.ui.screens.HomeScreen.ImagenesEnumeration


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

    // NUEVO: data para las secciones
    categories: List<CategoryItem> = defaultCategories(),
    artisanBanners: List<ArtisanBanner> = sampleArtisanBanners(),
    newThisMonth: List<Product> = emptyList(),
    @DrawableRes seasonalBannerRes: Int = R.drawable.logo_artemayor_horizontal,
    onOpenArtisan: (ArtisanBanner) -> Unit = {},
    onOpenProduct: (Product) -> Unit = {},
    onSeeAllNew: () -> Unit = {},
) {
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_artemayor_horizontal),
                        contentDescription = "Arte Mayor"
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
        bottomBar = { BottomBar(onPublishClick = onOpenPublish) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Buscador
            item {
                val cafeOscuro = Color(0xFF6B3F2C) // barra
                val cafePill = Color(0xFF87513A) // pill (un tono más claro)

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),                      // alto tipo "pill"
                    singleLine = true,
                    placeholder = { Text("¿Qué artesanía buscas?") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    trailingIcon = {
                        TextButton(onClick = { onSearch(query) }) { Text("Buscar") }
                    },
                    shape = RoundedCornerShape(28.dp),       // bordes redondeados
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = cafePill,    // fondo de la pill
                        unfocusedContainerColor = cafePill,
                        disabledContainerColor = cafePill,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedPlaceholderColor = Color(0xFFEFEFEF),
                        unfocusedPlaceholderColor = Color(0xFFEFEFEF),
                        focusedLeadingIconColor = Color.White,
                        unfocusedLeadingIconColor = Color.White,
                        focusedTrailingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch(query) })
                )
            }

            // CATEGORÍAS (carrusel)
            item { SectionTitle("Categorías") }
            item {
                CategoryCarousel(
                    items = categories,
                    onClick = { onCategoryClick(it.type) },

                    )
            }

            // CONOCE A NUESTROS ARTESANOS/AS (banners laterales)
            item { SectionTitle("Conoce a nuestros artesanos y artesanas") }
            item {
                ArtisanBannerRow(
                    banners = artisanBanners,
                    onClick = onOpenArtisan
                )
            }

            // LO NUEVO DE ESTE MES (productos recientes)
            item {
                SectionTitle(
                    title = "Lo nuevo de este mes",
                    trailing = { TextButton(onClick = onSeeAllNew) { Text("Ver todo") } }
                )
            }
            item {
                ProductRow(
                    products = newThisMonth,
                    onClick = onOpenProduct
                )
            }

            // ESPECIAL DE TEMPORADA (banner)
            item { SectionTitle("Especial de temporada") }
            item {
                SeasonalBanner(resId = seasonalBannerRes)
            }

            item { Spacer(Modifier.height(28.dp)) }
        }
    }
}
//Cierre fun HomeScreen


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
    maskBottomFraction: Float = 0.18f // franja inferior que se tapa
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
        // 👇 “Faldilla” que tapa lo de abajo
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(boxSize * maskBottomFraction)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}


@Composable
private fun CategoryCarousel(
    items: List<CategoryItem>,
    onClick: (CategoryItem) -> Unit,
    itemSize: Dp = 44.dp,
    itemSpacing: Dp = 10.dp,
    cropBottomFraction: Float = 0.12f,
    showLabels: Boolean = true
) {
    val state = rememberLazyListState()
    val fling = rememberSnapFlingBehavior(state)

    LazyRow(
        state = state,
        flingBehavior = fling,
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(items) { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(itemSize + 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(itemSize)
                        .clickable { onClick(item) }
                        .semantics { contentDescription = item.label },
                    contentAlignment = Alignment.Center
                ) {
                    CroppedCategoryImage(
                        resId = item.iconRes,
                        boxSize = itemSize,
                        bottomCutFraction = cropBottomFraction
                    )
                }

                if (showLabels) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = item.label,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


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
//                        painter = painterResource(b.imageRes),
                        if (b.title == ImagenesEnumeration.ArtesanosDelSur.nombre) {

                            painterResource(ImagenesEnumeration.ArtesanosDelSur.imgLoc)
                        }
                        else if (b.title == ImagenesEnumeration.TejedorasDeChiloe.nombre) {

                            painterResource(ImagenesEnumeration.TejedorasDeChiloe.imgLoc)
                        }
                        else if (b.title == ImagenesEnumeration.MadererosDelMaule.nombre) {
                            painterResource(ImagenesEnumeration.MadererosDelMaule.imgLoc)

                        } else {
                            painterResource(b.imageRes)

                        },
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
                // Imagen real aquí si tienes URL (Coil)
                // AsyncImage(model = product.imageUrl, contentDescription = product.name, ...)
                // Precio como pill
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
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
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
            // Subtítulo corto opcional (autor o categoría)
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

// ------------------------------ DATA DE MUESTRA ------------------------------

private fun defaultCategories() = listOf(
    CategoryItem("Lana", R.drawable.lana_icon, ProductType.LANA),
    CategoryItem("Madera", R.drawable.madera_icon, ProductType.MADERA),
    CategoryItem("Cerámica", R.drawable.ceramica_icon, ProductType.CERAMICA),
    CategoryItem("Greda", R.drawable.greda_icon, ProductType.GREDA),
    CategoryItem("Hilo", R.drawable.hilo_icon, ProductType.HILO),
    CategoryItem("Pintura", R.drawable.pintura_icon, ProductType.PINTURA),
)

private fun sampleArtisanBanners() = listOf(
    ArtisanBanner("a1", "Artesanos del Sur", R.drawable.logo_artemayor_horizontal),
    ArtisanBanner("a2", "Tejedoras de Chiloé", R.drawable.logo_artemayor_horizontal),
    ArtisanBanner("a3", "Madereros del Maule", R.drawable.logo_artemayor_horizontal),
)

