package com.example.ama.ui.screens.subcategory

import android.R.attr.minWidth
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.SUBCATS
import com.example.ama.ui.components.label
import com.example.ama.ui.components.prettyLabel
import com.example.ama.R


@OptIn(ExperimentalMaterial3Api::class)
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
    val cafe = Color(0xFF6B3F2C)
    val cafeClaro = Color(0xFF87513A)
    var q by remember { mutableStateOf("") }
    val subcats = remember(category) { SUBCATS[category] ?: emptyList() }

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
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Pill de búsqueda
            item {
                OutlinedTextField(
                    value = q,
                    onValueChange = { q = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    singleLine = true,
                    placeholder = { Text("¿Qué artesanía buscas?") },
                    leadingIcon = { Icon(Icons.Outlined.Search, null) },
                    trailingIcon = { TextButton(onClick = { onSearch(q) }) { Text("Buscar") } },
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = cafeClaro,
                        unfocusedContainerColor = cafeClaro,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLeadingIconColor = Color.White,
                        unfocusedLeadingIconColor = Color.White,
                        focusedTrailingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White,
                        focusedPlaceholderColor = Color(0xFFEFEFEF),
                        unfocusedPlaceholderColor = Color(0xFFEFEFEF),
                    )
                )
            }

            // Título
            item {
                Text(
                    text = category.prettyLabel(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 2.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy( // antes era titleMedium
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Chips redondeados
            val chipW = 104.dp
            val chipH = 32.dp

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                ) {
                    FilterPill("Ordenar\ny filtrar", width = chipW, height = chipH, twoLines = true) { /* TODO */ }
                    FilterPill("Región",             width = chipW, height = chipH) { /* TODO */ }
                    FilterPill("Precio",             width = chipW, height = chipH) { /* TODO */ }
                }
                Spacer(Modifier.height(20.dp))
            }


            // Lista de subcategorías (botones anchos)
            items(subcats.filter { q.isBlank() || it.label().contains(q, true) }) { sub ->
                Button(
                    onClick = { navController.navigate("catalog?type=${category.name}&sub=${sub.name}") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B3F2C),
                        contentColor = Color.White
                    ),
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        sub.label(),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.height(10.dp))              // separación uniforme
            }
        }
    }
}
@Composable
private fun FilterPill(
    label: String,
    width: Dp,
    height: Dp,
    twoLines: Boolean = false,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = label.uppercase(),
                maxLines = if (twoLines) 2 else 1,
                softWrap = twoLines,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall, // más compacto
                lineHeight = 12.sp
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
        },
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .width(width)   // 👈 ancho fijo corto
            .height(height),// 👈 alto compacto
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color(0xFF87513A),
            labelColor = Color.White,
            leadingIconContentColor = Color.White,
            trailingIconContentColor = Color.White
        )
    )
}
