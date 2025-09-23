package com.example.ama.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, //SOLO PARA BOTON DE PRUEBA
    cartCount: Int,
    onOpenCart: () -> Unit,
    onSearch: (String) -> Unit,
    onCategoryClick: (ProductType) -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit,
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
                // 👈 Botón de Configuración a la izquierda
                navigationIcon = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Configuración"
                        )
                    }
                },
                // 👉 Solo carrito a la derecha
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
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("¿Qué artesanía buscas?") },
                singleLine = true,
                trailingIcon = { TextButton(onClick = { onSearch(query) }) { Text("Buscar") } }
            )
//            Boton para probar la navegacion a ProductTypeScreen:
            Button(
                onClick = {
                    navController.navigate("productType")
                 },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Boton de prueba: Tipo de producto")
            }

            CategoryGrid(
                items = listOf(
                    CategoryItem("Lana",     R.drawable.lana_icon,     ProductType.TEXTIL),
                    CategoryItem("Madera",   R.drawable.madera_icon,   ProductType.MADERA),
                    CategoryItem("Cerámica", R.drawable.ceramica_icon, ProductType.CERAMICA),
                    CategoryItem("Greda",    R.drawable.greda_icon,    ProductType.GREDA),
                    CategoryItem("Hilo",     R.drawable.hilo_icon,     ProductType.HILO),
                    CategoryItem("Pintura",  R.drawable.pintura_icon,  ProductType.PINTURA),
                ),
                onClick = { onCategoryClick(it.type) },
                showLabels = false,
                chipSize = 120.dp,
                iconSize = 72.dp,
                showCircle = false
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}


data class CategoryItem(val label: String, @DrawableRes val iconRes: Int, val type: ProductType)

@Composable
fun CategoryGrid(
    items: List<CategoryItem>,
    onClick: (CategoryItem) -> Unit,
    showLabels: Boolean = false,
    chipSize: Dp = 120.dp,
    iconSize: Dp = 72.dp,
    showCircle: Boolean = false,
    circleColor: Color = Color.Transparent
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        items.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { item ->
                    CategoryChip(
                        label = item.label,
                        iconRes = item.iconRes,
                        onClick = { onClick(item) },
                        chipSize = chipSize,
                        iconSize = iconSize,
                        showCircle = showCircle,
                        circleColor = circleColor,
                        showLabel = showLabels
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryChip(
    label: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    chipSize: Dp,
    iconSize: Dp,
    showCircle: Boolean,
    circleColor: Color,
    showLabel: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val bg = if (showCircle) circleColor else Color.Transparent

        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = bg,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier
                .size(chipSize)
                .semantics { contentDescription = label }  // accesibilidad
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    contentScale = ContentScale.Fit
                )
            }
        }

        if (showLabel) {
            Spacer(Modifier.height(6.dp))
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}
