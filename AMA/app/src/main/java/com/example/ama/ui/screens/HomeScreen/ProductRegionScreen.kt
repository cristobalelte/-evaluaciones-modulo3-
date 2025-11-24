package com.example.ama.ui.screens.HomeScreen

import android.R.attr.category
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.SUBCATS
import com.example.ama.ui.components.label
import com.example.ama.ui.navigation.Routes

//Ruta = "regionScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRegionScreen(
    navController: NavController,
    category: ProductType,
    cartCount: Int,
    onOpenCart: () -> Unit,
    onOpenPublish: () -> Unit,
    onOpenSettings: () -> Unit,
    onSearch: (String) -> Unit
) {
    val cafe = Color(0xFF6B3F2C)
    val cafeClaro = Color(0xFF87513A)
    var q by remember { mutableStateOf("") }
    val subcats = remember(category) { SUBCATS[category] ?: emptyList() }
    var query by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_artemayor_horizontal),
                        contentDescription = "Arte Mayor",
                        modifier = Modifier.clickable(onClick = { navController.navigate(Routes.HOME)})
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
        bottomBar = { BottomBar(onPublishClick = onOpenPublish,
            onProfileClick = { navController.navigate("perfil") }) }
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
                    text = "Region",
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
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    FilterPill(
                        navController = navController,
                        "Ordenar\ny filtrar",
                        width = chipW,
                        height = chipH,
                        twoLines = true
                    )
                    { /* TODO */ }
                    FilterPill(
                        navController = navController,
                        width = chipW,
                        height = chipH,
                        label = "Region"
                    )
                    { /* TODO */ }
                    FilterPill(
                        navController = navController, "Precio", width = chipW, height = chipH
                    )
                    { /* TODO */ }
                }
                Spacer(Modifier.height(20.dp))
            }


//            Row 1 de 3 botones:
            /* Row(
                 modifier = Modifier
                     .padding(4.dp)
                     .fillMaxWidth()
                     .weight(1f)
                     .horizontalScroll(rememberScrollState()),
                 horizontalArrangement = Arrangement.SpaceEvenly
             ) {
                 Button(
                     onClick = {
                         navController.navigate("productType")
                     },
                     colors = ButtonDefaults.buttonColors(
                         containerColor = MaterialTheme.colorScheme.secondary,
                         contentColor = MaterialTheme.colorScheme.onSecondary
                     ),
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(2.dp)
                         .weight(1f)
                 ) {
                     Text(
                         textAlign = TextAlign.Center,
                         style = MaterialTheme.typography.bodySmall,
                         text =  "Ordenar y.."
                     )
                 }

                 Button(
                     onClick = {
                         navController.navigate("productType")
                         val text = "Abriendo Filtro"
                         val duration: Int = Toast.LENGTH_SHORT
                         Toast.makeText(navController.context, text, duration).show()
                     },
                     colors = ButtonDefaults.buttonColors(
                         containerColor = MaterialTheme.colorScheme.secondary,
                         contentColor = MaterialTheme.colorScheme.onSecondary
                     ),
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(2.dp)
                         .weight(1f)


                 ) {
                     Text(
                         textAlign = TextAlign.Center,
                         style = MaterialTheme.typography.bodySmall,
                         text = "Region"
                     )
                 }

                 Button(
                     onClick = {
                         navController.navigate("productType")
                         val text = "Abriendo Filtro"
                         val duration: Int = Toast.LENGTH_SHORT
                         Toast.makeText(navController.context, text, duration).show()
                     },
                     colors = ButtonDefaults.buttonColors(
                         containerColor = MaterialTheme.colorScheme.secondary,
                         contentColor = MaterialTheme.colorScheme.onSecondary
                     ),
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(2.dp)
                         .weight(1f)
                 ) {
                     Text(
                         textAlign = TextAlign.Center,
                         style = MaterialTheme.typography.bodySmall,
                         text = "Precio"
                     )
                 }


             } //Cierre Row 1*/

//            Row 1 de 3 botones:
            /*            Row(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("productType")
                                    val text = "Abriendo Filtro"
                                    val duration: Int = Toast.LENGTH_SHORT
                                    Toast.makeText(navController.context, text, duration).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall,
                                    text = "ARICA Y PARINACOTA"
                                )
                            }

                            Button(
                                onClick = {
                                    navController.navigate("productType")
                                    val text = "Abriendo Filtro"
                                    val duration: Int = Toast.LENGTH_SHORT
                                    Toast.makeText(navController.context, text, duration).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall,
                                    text = "TARAPACA"
                                )
                            }

                            Button(
                                onClick = {
                                    navController.navigate("productType")
                                    val text = "Abriendo Filtro"
                                    val duration: Int = Toast.LENGTH_SHORT
                                    Toast.makeText(navController.context, text, duration).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall,
                                    text = "ANTOFAGASTA"
                                )
                            }

                        } //Cierre Row 2*/

//            Row 2: 3 elementos:

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(
                        onClick = {
                            navController.navigate("guantes")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "ATACAMA"
                        )
                    }



                    Button(
                        onClick = {
                            navController.navigate("chalecos")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "COQUIMBO"
                        )
                    }


                    Button(
                        onClick = {
                            navController.navigate("chalecos")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "VALPARAISO"
                        )
                    }

                } //Cierre Row 3
            }

            item {

//            Row 3 de 3 elementos:
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate("gorros")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "RM"
                        )
                    }


                    Button(
                        onClick = {
                            navController.navigate("calcetines")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "O'HIGGINS"
                        )
                    }


                    Button(
                        onClick = {
                            navController.navigate("calcetines")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "MAULE"
                        )
                    }

                } //Cierre Row 3
            }

            item {
//            Row 4 de 3 elementos:
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate("ponchos")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "ÑUBLE"
                        )
                    }


                    Button(
                        onClick = {
                            navController.navigate("mantas")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "BIOBIO"
                        )
                    }


                    Button(
                        onClick = {
                            navController.navigate("mantas")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "LOS LAGOS"
                        )
                    }

                } //Cierre Row 4
            }

            item {
//            ROw 5 de 2 elementos:
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            navController.navigate("ponchos")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "AYSEN"
                        )
                    }


                    Button(
                        onClick = {
                            navController.navigate("mantas")
                            val text = "Abriendo Filtro"
                            val duration: Int = Toast.LENGTH_SHORT
                            Toast.makeText(navController.context, text, duration).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .weight(1f)
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            text = "MAGALLANES"
                        )
                    }

                } //Cierre Row 5
            }

            items(subcats.filter { q.isBlank() || it.label().contains(q, true) })
            { sub ->
//            Boton ir a los resultados y Volver:
                Button(
                    onClick = {
                        navController.navigate("catalog?type=${category.name}&sub=${sub.name}")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "IR A LOS RESULTADOS"
                    )
                }

                Button(
                    onClick = {
                        navController.popBackStack()
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "VOLVER"
                    )
                }

                Button(
                    onClick = {
                        navController.navigate("datosEnvio")
                        val text = "Abriendo Filtro"
                        val duration: Int = Toast.LENGTH_SHORT
                        Toast.makeText(navController.context, text, duration).show()
                    },
//                    enabled = items.isNotEmpty(),
                    //Cambio de shape btn proceder al pago:
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        text = "CONTINUAR COMPRA"
                    )
                }

            }


        }
    }
}

@Composable
private fun FilterPill(
    navController: NavController,
    label: String,
    width: Dp,
    height: Dp,
    twoLines: Boolean = false,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = {
            navController.navigate("regionScreen")

        },
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
