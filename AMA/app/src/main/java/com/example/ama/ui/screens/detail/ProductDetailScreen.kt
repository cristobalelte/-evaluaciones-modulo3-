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
import java.text.NumberFormat
import java.util.Locale
import com.example.ama.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit,
    onAddToCart: (Product) -> Unit,

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
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp
            )


            val autorTexto =
                if (!publishedBy.isNullOrBlank() && publishedBy != product.author) {
                    "Publicado por: $publishedBy\nAutor/a real: ${product.author}"
                } else {
                    "Autor/a: ${product.author}"
                }
            Text(text = autorTexto, style = MaterialTheme.typography.bodyMedium, fontSize = 15.sp)


            Text(
                text = description ?: stringResource(R.string.app_description),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            )
            Text("Región: ${product.region}", style = MaterialTheme.typography.bodyMedium, fontSize = 15.sp)
            Text(
                "Tipo: " + when(product.type) {
                    ProductType.TEXTIL -> stringResource(R.string.Textil_prod)
                    ProductType.MADERA -> stringResource(R.string.Madera_prod)
                    ProductType.CERAMICA -> stringResource(R.string.Ceramica_prod)
                    ProductType.OTRO -> stringResource(R.string.Otro_prod)
                    ProductType.GREDA -> TODO()
                    ProductType.HILO -> TODO()
                    ProductType.PINTURA -> TODO()
                },
                fontSize = 20.sp,
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
                Text(stringResource(R.string.add_to_cart),
                    fontSize = 20.sp)
            }
        }
    }
}


