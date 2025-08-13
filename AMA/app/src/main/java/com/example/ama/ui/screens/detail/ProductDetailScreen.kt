package com.example.ama.ui.screens.detail



import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ama.ui.screens.catalog.Product
import java.text.NumberFormat
import java.util.Locale
import com.example.ama.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.ama.ui.screens.catalog.ProductType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit,
    onAddToCart: (Product) -> Unit,
    // opcionales (para HU: intermediario/descripcion si luego los agregan)
    publishedBy: String? = null,
    description: String? = null
) {
    val currency = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.pop_back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen principal (tu modelo usa imageUrl simple)
            // si no hay URL, usa el drawable local
            val img: Any = product.imageUrl.takeIf { it.isNotBlank() }
                ?: R.drawable.placeholder_image



            AsyncImage(
                model = img,                                  // ← ahora acepta String o Int
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder_image),
                error = painterResource(R.drawable.placeholder_image)
            )

            // Precio
            Text(
                text = currency.format(product.price),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // Autor real vs publicado por (si te pasan publishedBy)
            val autorTexto =
                if (!publishedBy.isNullOrBlank() && publishedBy != product.author) {
                    "Publicado por: $publishedBy\nAutor/a real: ${product.author}"
                } else {
                    "Autor/a: ${product.author}"
                }
            Text(text = autorTexto, style = MaterialTheme.typography.bodyMedium)

            // Descripción (si te la pasan; si no, un texto amigable)
            Text(
                text = description ?: stringResource(R.string.app_description),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
            Text("Región: ${product.region}", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Tipo: " + when(product.type) {
                    ProductType.TEXTIL -> stringResource(R.string.Textil_prod)
                    ProductType.MADERA -> stringResource(R.string.Madera_prod)
                    ProductType.CERAMICA -> stringResource(R.string.Ceramica_prod)
                    ProductType.OTRO -> stringResource(R.string.Otro_prod)
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.weight(1f))

            // Botón agregar al carrito
            Button(
                onClick = { onAddToCart(product) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.add_to_cart))
            }
        }
    }
}


