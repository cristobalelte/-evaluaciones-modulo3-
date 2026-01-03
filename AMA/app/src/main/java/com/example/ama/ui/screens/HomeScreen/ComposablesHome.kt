package com.example.ama.ui.screens.HomeScreen

import ArtisanBannerUi
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ama.core.dto.CategoryDto
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.priceFormatted
import java.text.NumberFormat
import java.util.Locale

private const val BASE_URL = "http://44.222.218.77:3000" // ajusta si cambia

fun fullImageUrl(path: String?): String {
    val p = path?.trim().orEmpty()
    if (p.isBlank()) return ""              // sin imagen -> vacío
    if (p.startsWith("http")) return p      // ya es absoluta
    return BASE_URL + p                     // "/storage/..." -> absoluta
}
@Composable
fun SafeImageRes(
    @DrawableRes resId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    fallbackColor: Color = Color(0xFFEFEFEF)
) {
    if (resId == 0) {
        Box(modifier = modifier.background(fallbackColor))
    } else {
        Image(
            painter = painterResource(resId),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

/* --------------------------- UI HELPERS --------------------------- */

@Composable
fun SectionTitle(
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
fun CategoriesCarouselFigma(
    categories: List<CategoryDto>,
    onCategoryClickCategoryId: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sidePadding = 16.dp
    val spacing = 8.dp          // 🔹 más juntos
    val chipSize = 96.dp        // 🔹 más grandes (clave)

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = sidePadding),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        items(
            items = categories,
            key = { (it.id ?: it.name)!! }
        ) { c ->
            CategoryChipFigma(
                modifier = Modifier.width(chipSize),
                imageSize = 56.dp, // imagen más grande
                name = c.name,
                imageUrl = fullImageUrl(c.imageUrl ?: ""),
                onClick = {
                    c.id?.toIntOrNull()?.let(onCategoryClickCategoryId)
                }
            )
        }
    }
}


@Composable
fun CategoryChipFigma(
    name: String?,
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageSize: Dp = 56.dp
) {
    Column(
        modifier = modifier
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp) // 🔹 círculo grande como Figma
                .clip(CircleShape)
                .background(Color(0xFFF4F4F4)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier.size(imageSize),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (name != null) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ArtisanBannerRowRemote(
    banners: List<ArtisanBannerUi>,
    onClick: (ArtisanBannerUi) -> Unit
) {
    val listState = rememberLazyListState()

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val sidePadding = 16.dp
        val spacing = 12.dp

        // 1 card visible + "peek" (ajusta 0.84f si quieres más/menos peek)
        val cardWidth = maxWidth * 0.84f
        val cardHeight = 170.dp

        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = sidePadding),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            itemsIndexed(
                items = banners,
                key = { index, b -> "${b.id}-${b.title}-$index" } // <-- SIEMPRE String (evita errores raros)
            ) { _, b ->
                ArtisanBannerCard(
                    b = b,
                    width = cardWidth,
                    height = cardHeight,
                    onClick = { onClick(b) }
                )
            }
        }
    }
}

@Composable
private fun ArtisanBannerCard(
    b: ArtisanBannerUi,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(shape)
            .background(Color(0xFFF2F2F2))
            .clickable { onClick() }
    ) {
        // Imagen (si existe)
        if (!b.imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = b.imageUrl,
                contentDescription = b.title,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Franja inferior con texto (siempre visible)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(Color(0x99000000))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = b.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (b.subtitle.isNotBlank()) {
                    Text(
                        text = b.subtitle,
                        color = Color.White.copy(alpha = 0.90f),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


/* --------------------------- PRODUCTS --------------------------- */

@Composable
fun ProductRow(
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
fun NuevoMesCard(
    product: Product,
    onFavClick: (Product) -> Unit = {},
    onClick: (Product) -> Unit = {}
) {
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(product) }
    ) {
        // Imagen + overlays
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // cuadrado como tu mock
                .clip(shape)
                .background(Color(0xFFF2F2F2))
        ) {
            // Imagen
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Corazón (arriba derecha)
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.9f),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(28.dp)
                    .clickable { onFavClick(product) }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = Color(0xFF555555),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Chip "Nuevo" (abajo izquierda)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFB300)) // amarillo/naranjo del mock
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Nuevo",
                    color = Color.Black,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Precio
        Text(
            text = formatCLP(product.price),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        // Nombre (2 líneas aprox como mock)
        Text(
            text = product.name,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF555555),
            maxLines = 2
        )
    }
}

private fun formatCLP(value: Double): String {
    val nf = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    nf.maximumFractionDigits = 0
    return nf.format(value)
}
@Composable
fun SeasonalBanner(@DrawableRes resId: Int) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        SafeImageRes(
            resId = resId,
            contentDescription = "Especial de temporada",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}