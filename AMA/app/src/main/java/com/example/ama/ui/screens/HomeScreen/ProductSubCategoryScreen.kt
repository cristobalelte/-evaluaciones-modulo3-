package com.example.ama.ui.screens.subcategory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.data.viewmodel.AddProductViewModel
import com.example.ama.data.viewmodel.AddProductViewModelFactory
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.SUBCATS
import com.example.ama.ui.components.TopBar
import com.example.ama.ui.components.label
import com.example.ama.ui.components.prettyLabel

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
    var q by remember { mutableStateOf("") }

    val allSubcats = remember(category) { SUBCATS[category] ?: emptyList() }
    val filtered = remember(q, allSubcats) {
        allSubcats
            .filter { q.isBlank() || it.label().contains(q, ignoreCase = true) }
            .sortedBy { it.label() }
    }

//    Para expandir desplegables:
    var expandedPrecio by remember { mutableStateOf(false) }
    var expandedRegion by remember { mutableStateOf(false) }
    var expandedType by remember { mutableStateOf(false) }
    var expandedMaterial by remember { mutableStateOf(false) }


    val addProdVM: AddProductViewModel = viewModel(
        factory = AddProductViewModelFactory()
    )

    // Colores neutros como el mock
    val pageBg = Color.White
    val rowBg = Color(0xFFF6EDED) // gris suave
    val gapBg = Color.White       // separación blanca entre filas

//    Ventana emergente:
    var showPopup by remember { mutableStateOf(false) }
    // 3. Usa el componente Popup. Si la var bool showPopup es true, muestra el popup
    if (showPopup) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = { showPopup = false } // Cierra la ventana al tocar fuera
        )
        {
            // Fondo oscuro y difuminado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.6f)), // Color negro semitransparente
                contentAlignment = Alignment.BottomCenter
            )
            {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .verticalScroll(rememberScrollState())
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp
                    ), // Bordes redondeados
                    tonalElevation = 8.dp,
                    color = Color.White
                )
                {
                    // Define el contenido de la ventana emergente aquí
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    )
                    {
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 8.dp),
                            text = "Ordenar y filtrar",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(5.dp))


//                  DESPLEGABLE 1: PRECIO:
                        ExposedDropdownMenuBox(
                            expanded = expandedPrecio,
                            onExpandedChange = { expandedPrecio = !expandedPrecio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            TextField(
                                //Delegamos a la clase AddProductViewModel los valores de value y onValueChange,
                                // para que estos no se borran por ej al rotar la pantalla:
                                value = addProdVM.precios,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Precio") },
                                trailingIcon = { TrailingIcon(expanded = expandedPrecio) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                                // Importante para que funcione correctamente

                            )
                            ExposedDropdownMenu(
                                expanded = expandedPrecio,
                                onDismissRequest = { expandedPrecio = false }
                            ) {
                                addProdVM.priceOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt) },
                                        onClick = {
                                            //Delegamos a la clase viewModel: AddProductViewModel para que al hacer click,
                                            // cambie el valor del desplegable por el texto elegido que esta en
                                            // AddProductViewModel.onProductCategoryChange():
                                            addProdVM.onPriceTypeChange(opt)
                                            expandedPrecio = false
                                        }
                                    )
                                }
                            }
                        }
                        //Fin lista desplegable 1

                        Spacer(Modifier.height(5.dp))


                        //                  DESPLEGABLE 2: REGION:
                        ExposedDropdownMenuBox(
                            expanded = expandedRegion,
                            onExpandedChange = { expandedRegion = !expandedRegion },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            TextField(
                                //Delegamos a la clase AddProductViewModel los valores de value y onValueChange,
                                // para que estos no se borran por ej al rotar la pantalla:
                                value = addProdVM.regiones,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Region") },
                                trailingIcon = { TrailingIcon(expanded = expandedRegion) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                                // Importante para que funcione correctamente

                            )
                            ExposedDropdownMenu(
                                expanded = expandedRegion,
                                onDismissRequest = { expandedRegion = false }
                            ) {
                                addProdVM.regionOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt) },
                                        onClick = {
                                            //Delegamos a la clase viewModel: AddProductViewModel para que al hacer click,
                                            // cambie el valor del desplegable por el texto elegido que esta en
                                            // AddProductViewModel.onProductCategoryChange():
                                            addProdVM.onRegionChange(opt)
                                            expandedRegion = false
                                        }
                                    )
                                }
                            }
                        }
                        //Fin lista desplegable 2

                        Spacer(Modifier.height(5.dp))


                        //                  DESPLEGABLE 3: Tipo de artesania:
                        ExposedDropdownMenuBox(
                            expanded = expandedType,
                            onExpandedChange = { expandedType = !expandedType },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            TextField(
                                //Delegamos a la clase AddProductViewModel los valores de value y onValueChange,
                                // para que estos no se borran por ej al rotar la pantalla:
                                value = addProdVM.types,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Tipo de artesania") },
                                trailingIcon = { TrailingIcon(expanded = expandedType) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                                // Importante para que funcione correctamente

                            )
                            ExposedDropdownMenu(
                                expanded = expandedType,
                                onDismissRequest = { expandedType = false }
                            ) {
                                addProdVM.typeOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt) },
                                        onClick = {
                                            //Delegamos a la clase viewModel: AddProductViewModel para que al hacer click,
                                            // cambie el valor del desplegable por el texto elegido que esta en
                                            // AddProductViewModel.onProductCategoryChange():
                                            addProdVM.onTypeChange(opt)
                                            expandedType = false
                                        }
                                    )
                                }
                            }
                        }
                        //Fin lista desplegable 3

                        Spacer(Modifier.height(5.dp))

                        //               DESPLEGABLE 4: Material de artesania:
                        ExposedDropdownMenuBox(
                            expanded = expandedMaterial,
                            onExpandedChange = { expandedMaterial = !expandedMaterial },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            TextField(
                                //Delegamos a la clase AddProductViewModel los valores de value y onValueChange,
                                // para que estos no se borran por ej al rotar la pantalla:
                                value = addProdVM.materials,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Material de artesania") },
                                trailingIcon = { TrailingIcon(expanded = expandedMaterial) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                                // Importante para que funcione correctamente

                            )
                            ExposedDropdownMenu(
                                expanded = expandedMaterial,
                                onDismissRequest = { expandedMaterial = false }
                            ) {
                                addProdVM.materialOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt) },
                                        onClick = {
                                            //Delegamos a la clase viewModel: AddProductViewModel para que al hacer click,
                                            // cambie el valor del desplegable por el texto elegido que esta en
                                            // AddProductViewModel.onProductCategoryChange():
                                            addProdVM.onMaterialChange(opt)
                                            expandedMaterial = false
                                        }
                                    )
                                }
                            }
                        }
                        //Fin lista desplegable 4

//                        Botones: Ir a los resultados y Volver:
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC107),
                                contentColor = Color(0xFF7B001A)
                            ),
                            onClick = {
                                showPopup = false
//                                navController.navigate(Routes.CART)
                            },
                            modifier = Modifier
                                .width(200.dp) // Define el ancho en 200 dp
                                .border(
                                    2.dp,
                                    Color.Yellow,
                                    shape = RoundedCornerShape(24.dp)
                                ) // Grosor, color y forma
                        )
                        {
                            Text(
                                text = "Ir a los resultados"
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

//                Se agrega producto al carrito y se cierra el popup:
//                pero no vamos al carrito, sino que volvemos a la pantalla anterior:
                        Button(
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            onClick = {
                                showPopup = false
                            },
                            modifier = Modifier
                                .width(200.dp) // Define el ancho en 200 dp
                                .border(
                                    2.dp,
                                    Color.Yellow,
                                    shape = RoundedCornerShape(24.dp)
                                ) // Grosor, color y forma

                        )
                        {
                            Text(
                                text = "Volver"
                            )
                        }


                    } //Cierre Column
                } //Cierre Surface
            } //Cierre Box
        } //Cierre popup
    } //Cierre if

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
                    placeholder = {
                        Text(
                            "¿Qué artesanía buscas?",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
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


            // "Icono + Ver filtros" + título categoria centrado
            item {
                var leftWidthPx by remember { mutableIntStateOf(0) }
                val density = LocalDensity.current

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 36.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Izquierda: Icono + Ver filtros (medimos su ancho)
                    Row(
                        modifier = Modifier
                            .onGloballyPositioned { leftWidthPx = it.size.width }
//                   Hay que cambiar la sgte linea para quer vaya a una nueva pantalla emergente de filtros:
//                            .clickable { navController.navigate("regionScreen") },
                            .clickable { showPopup = true },
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

                    // Centro: título categoria centrado REAL
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