package com.example.ama.ui.screens.HomeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.SUBCATS
import com.example.ama.ui.components.TopBar
import com.example.ama.ui.components.label
import com.example.ama.ui.components.prettyLabel

@Composable
fun ProductSubCatScreen(
    navController: NavController,
    category: ProductType,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onOpenPublish: () -> Unit,
    onBack: () -> Unit,
    onSearch: (String) -> Unit = {}
) {
    var q by remember { mutableStateOf("") }

    val allSubcats = remember(category) { SUBCATS[category] ?: emptyList() }
    val filtered = remember(q, allSubcats) {
        allSubcats
            .filter { q.isBlank() || it.label().contains(q, ignoreCase = true) }
            .sortedBy { it.label() }
    }

    // Colores neutros como el mock
    val pageBg = Color.White
    val rowBg  = Color(0xFFF6EDED) // gris suave
    val gapBg  = Color.White       // separación blanca entre filas

    Scaffold(
        containerColor = pageBg,
        topBar = { TopBar(navController, cartCount, onOpenCart) },
        bottomBar = {
            BottomBar(
                navController = navController,
                onPublishClick = onOpenPublish,
                onProfileClick = { navController.navigate("perfil") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
        ) {

            // "Categorías | Lana" (Lana subrayado como el mock)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "  |  ",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = category.prettyLabel(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(10.dp))
            }

            // Buscador
            item {
                OutlinedTextField(
                    value = q,
                    onValueChange = { q = it; onSearch(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = { Text("¿Qué artesanía buscas?", style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    shape = MaterialTheme.shapes.large,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
                Spacer(Modifier.height(10.dp))
            }


            // "Ver filtros" + título centrado
            item {
                var leftWidthPx by remember { mutableIntStateOf(0) }
                val density = LocalDensity.current

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 36.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Izquierda: Ver filtros (medimos su ancho)
                    Row(
                        modifier = Modifier
                            .onGloballyPositioned { leftWidthPx = it.size.width }
//                            Hay que cambiar la sgte linea para quer vaya a una nueva de filtros:
                            .clickable { navController.navigate("regionScreen") },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Tune,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Ver filtros",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Centro: título centrado REAL
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.prettyLabel(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Derecha: espacio “espejo” del ancho izquierdo para centrar perfecto
                    Spacer(
                        Modifier.width(with(density) { leftWidthPx.toDp() })
                    )
                }

                Spacer(Modifier.height(6.dp))
            }

            itemsIndexed(filtered) { index, sub ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = rowBg,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("catalog?type=${category.name}&sub=${sub.name}")
                            }
                            .heightIn(min = 44.dp) // ✅ altura más parecida al mock
                            .padding(horizontal = 16.dp, vertical = 10.dp), // ✅ menos alto
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sub.label(),
                            style = MaterialTheme.typography.bodyMedium, // ✅ texto más chico
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                //
                if (index != filtered.lastIndex) {
                    Spacer(Modifier.height(6.dp))
                }
            }
        }
    }
}

