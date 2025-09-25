// app/src/main/java/com/example/ama/ui/screens/detail/ProductDetailScreen.kt
package com.example.ama.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ama.R
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.label
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit,
    onAddToCart: (Product) -> Unit,
    publishedBy: String? = null,
    description: String? = null,
    cartCount: Int,
    onOpenCart: () -> Unit,
) {
    val currency = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    val typeText = remember(product.type) {
        when (product.type) {
            ProductType.LANA  -> "Lana"
            ProductType.MADERA  -> "Madera"
            ProductType.CERAMICA-> "Cerámica"
            ProductType.GREDA   -> "Greda"
            ProductType.HILO    -> "Hilo"
            ProductType.PINTURA -> "Pintura"
            ProductType.OTRO    -> "Otro"
        }
    }


    val images = remember(product.imageUrl) {
        val single = product.imageUrl.takeIf { it.isNotBlank() } ?: ""
        listOf(single).filter { it.isNotBlank() }
    }
    var index by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_artemayor_horizontal),
                        contentDescription = "Arte Mayor",
                        modifier = Modifier.height(40.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onOpenCart) {
                        BadgedBox(badge = {
                            if (cartCount > 0) Badge { Text("$cartCount") }
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = "Carrito"
                            )
                        }
                    }
                }
            )
        }
,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                val model: Any =
                    images.getOrNull(index) ?: R.drawable.placeholder_image

                AsyncImage(
                    model = model,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.1f),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.placeholder_image),
                    error = painterResource(R.drawable.placeholder_image)
                )

                if (images.size > 1) {
                    IconButton(
                        onClick = { index = (index - 1 + images.size) % images.size },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    ) {
                        Icon(Icons.Outlined.ChevronLeft, contentDescription = "Anterior")
                    }
                    IconButton(
                        onClick = { index = (index + 1) % images.size },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    ) {
                        Icon(Icons.Outlined.ChevronRight, contentDescription = "Siguiente")
                    }
                }


                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    tonalElevation = 1.dp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                ) {
                    IconButton(onClick = { /* TODO: favorito */ }) {
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorito")
                    }
                }
            }


            Text(
                text = "Autor/a: ${product.author}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Título + precio
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = currency.format(product.price),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp
            )

            val descToShow = (description ?: product.description).ifBlank { "Sin descripción" }
            Text(
                text = descToShow,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            // Metadatos
            Text(
                "Región: ${product.region}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Tipo: $typeText",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Stock: ${product.stock}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Subcategoría: ${product.subcategory.label()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )


            Spacer(Modifier.weight(1f))


            Button(
                onClick = { onAddToCart(product) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.add_to_cart), fontSize = 18.sp)
            }
        }
    }
}



