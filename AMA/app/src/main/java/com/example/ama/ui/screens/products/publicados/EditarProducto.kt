package com.example.ama.ui.screens.products.publicados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ama.R
import com.example.ama.data.dataclass.ProductData
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.Product
import com.example.ama.ui.components.ProductType
import com.example.ama.ui.components.TopBar
import com.example.ama.ui.navigation.Routes
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProducto(
    navController: NavController,
    product: ProductData, //O ProductData?
    onBack: () -> Unit,
    onOpenPublish: () -> Unit,
    description: String? = null,
    cartCount: Int,
    onOpenCart: () -> Unit,
) {
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
                     .fillMaxSize()
                     .background(Color.Black.copy(alpha = 0.6f)), // Color negro semitransparente
                 contentAlignment = Alignment.Center
             )
             {
                 // Define el contenido de la ventana emergente aquí
                 Column(
                     modifier = Modifier
                         .padding(16.dp)
                         .fillMaxWidth()
                         .background(Color.White),
                     horizontalAlignment = Alignment.CenterHorizontally,
                     verticalArrangement = Arrangement.Center
                 )
                 {
                     Text(
                         textAlign = TextAlign.Center,
                         modifier = Modifier
                             .padding(top = 8.dp),
                         text = "Deseas editar este producto?",
                         style = MaterialTheme.typography.titleMedium,
                         color = Color.Black
                     )
                     product.name?.let {
                         Text(
                             text = it,
                             style = MaterialTheme.typography.titleSmall,
                             color = Color.Black
                         )
                     }
 //               Si deseo editar el producto, se cierra el popup
 //                y vamos al prod Detail:
                     Button(
                         onClick = {
                             showPopup = false //Cerrar popup
//                             onAddToCart(product)
//                             navController.navigate("detail/${product.id}") //Ir a detalle del producto con los cambios
                         },
                         modifier = Modifier
                             .padding(end = 8.dp)
                     )
                     {
                         Text(
                             text = "Si. Editar el producto",
                             color = Color.White
                         )
                     }

 //                Boton Cancelar:
                     // Se cierra el popup y volvemos a la pantalla anterior sin guardar los cambios:
                     Button(
                         colors = ButtonDefaults.buttonColors(
                             containerColor = Color.LightGray,
                             contentColor = Color.Black
                         ),
                         onClick = {
                             showPopup = false //Cerrar popup
//                             onAddToCart(product)
                         },
                         modifier = Modifier
                             .padding(end = 8.dp)
                     )
                     {
                         Text(
                             text = "Cancelar"
                         )
                     }


                 } //Cierre Column
             } //Cierre Box
         } // Cierre Popup
     } //Cierre if

    val currency = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

/*
    val typeText = remember(product.type) {
        when (product.type) {
            ProductType.LANA -> "Lana"
            ProductType.MADERA -> "Madera"
            ProductType.CERAMICA -> "Cerámica"
            ProductType.GREDA -> "Greda"
            ProductType.HILO -> "Hilo"
            ProductType.PINTURA -> "Pintura"
            ProductType.OTRO -> "Otro"
        }
    }
*/


    val images = remember(product.imageUrl) {
        val single = product.imageUrl.takeIf { it.isNotBlank() } ?: ""
        listOf(single).filter { it.isNotBlank() }
    }
    var index by remember { mutableIntStateOf(0) }



    Scaffold(
        topBar = {
            TopBar(
                navController,
                cartCount,
                onOpenCart
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                onHelpClick = {},
                onPublishClick = onOpenPublish,
                onProfileClick = { navController.navigate("profile") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                /*Operador Elvis ?:
               Si la parte izquierda (images.getOrNull(index)) es no nula, se usa ese valor.
               Si es nula, se usa el valor a la derecha del ?:.
               Resultado
               model obtiene la imagen correspondiente si existe.
               Si no existe, obtiene el recurso drawable placeholder_image.
               */

//                La var model es la imagen que se va a mostrar, tomada de la var images = product.imageUrl
//               Si no existe se muestra la imagen de placeholder_image:
                val model: Any =
                    images.getOrNull(index) ?: R.drawable.placeholder_image

                AsyncImage(
                    model = model, //Imagen obtenida de la var images = product.imageUrl
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


//                Imagen del corazon abajo a la derecha sobre la Imagen:
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
//Fin aspecto Imagen

//            Poner horizontalmente Nombre del Autor y Tipo: "Categoria $categoria":

            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                product.author?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "|",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.width(10.dp))

//    OJO: Cambiar el Text por un desplegable con las categorias:
                Text(
                    "Categoria ${product.craftType}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

            }
//            Cierre Row


            // Título:
            product.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

//             Precio:
            Text(
                text = currency.format(product.price),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp
            )

            val descToShow: String = product.description ?: "Sin descripcion"
            // Descripción
            Text(
                text = descToShow,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            // Metadatos
            /* Text(
                 "Región: ${product.region}",
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
 */

            Spacer(Modifier.weight(1f))

//          Botones Cancelar y Guardar Cambios:
            /*    Button(
                    onClick = { showPopup = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.add_to_cart), fontSize = 16.sp)
                }*/

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    onClick = {
                        onBack()
//                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Cancelar")
                }

                Button(
                    onClick = { /* Acción al hacer clic en el botón: Abrir Popup */
                       showPopup = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = 2.dp)
                ) {
                    Text(
                        text = "Guardar Cambios",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally) // Asegura que el texto se centre
                    )
                }
            }
        }
    }
}
