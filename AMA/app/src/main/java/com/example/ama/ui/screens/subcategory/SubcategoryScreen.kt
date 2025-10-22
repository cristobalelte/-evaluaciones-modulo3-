package com.example.ama.ui.screens.subcategory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.Subcategory
import com.example.ama.ui.components.SUBCATS
import com.example.ama.ui.components.label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubcategoryScreen(
    navController: NavController,
    onOpenPublish: () -> Unit,
    category: ProductType,
    onBack: () -> Unit,
    onOpenProducts: (Subcategory) -> Unit
) {
    var q by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(category.name.lowercase().replaceFirstChar { it.titlecase() }) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                onPublishClick = onOpenPublish,
                onProfileClick = { navController.navigate("perfil") }
            )
        }

    ) { padding ->
        val all = SUBCATS[category].orEmpty()
        val filtered = remember(q, all) {
            if (q.isBlank()) all
            else all.filter { it.label().contains(q, ignoreCase = true) }
        }

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Buscador tipo “pill”
            item {
                OutlinedTextField(
                    value = q,
                    onValueChange = { q = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    singleLine = true,
                    placeholder = { Text("Busca una subcategoría…") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent
                    )
                )
            }

            // (Opcional) Chips de filtro rápidos
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = { /* TODO ordenar */ }, label = { Text("Ordenar y filtrar") })
                    AssistChip(onClick = { /* TODO región  */ }, label = { Text("Región") })
                    AssistChip(onClick = { /* TODO precio  */ }, label = { Text("Precio") })
                }
            }

            items(filtered) { sub ->
                Button(
                    onClick = { onOpenProducts(sub) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(sub.label())
                }
            }
        }
    }
}
