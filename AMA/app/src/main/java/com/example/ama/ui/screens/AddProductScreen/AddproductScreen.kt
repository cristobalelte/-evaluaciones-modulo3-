// app/src/main/java/com/example/ama/ui/screens/publish/AddProductScreen.kt
package com.example.ama.ui.screens.publish

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ama.ui.components.ProductType

// --- Regiones de Chile ---
private val REGIONES_CHILE = listOf(
    "Arica y Parinacota",
    "Tarapacá",
    "Antofagasta",
    "Atacama",
    "Coquimbo",
    "Valparaíso",
    "Metropolitana de Santiago",
    "O’Higgins",
    "Maule",
    "Ñuble",
    "Biobío",
    "La Araucanía",
    "Los Ríos",
    "Los Lagos",
    "Aysén",
    "Magallanes y de la Antártica Chilena"
)

data class NewProduct(
    val name: String,
    val price: Double,
    val type: ProductType,
    val region: String,
    val stock: Int,
    val imageUri: Uri?,          // guardamos el URI local de la imagen
    val author: String,
    val description: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onPublish: (NewProduct) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var stockText by remember { mutableStateOf("1") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var type by remember { mutableStateOf(ProductType.LANA) }
    var typeExpanded by remember { mutableStateOf(false) }

    // dropdown de región
    var regionExpanded by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Photo Picker (Android 13+) + fallback GetContent
    val context = LocalContext.current
    val supportsPhotoPicker = ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(context)

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> selectedImageUri = uri }

    val getContentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri = uri }

    fun pickImage() {
        if (supportsPhotoPicker) {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            getContentLauncher.launch("image/*")
        }
    }

    val isValid = name.isNotBlank() &&
            priceText.toDoubleOrNull() != null &&
            region.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Publicar producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen (picker + preview)
            Text("Imagen", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ElevatedButton(onClick = { pickImage() }) { Text("Seleccionar de galería") }
                if (selectedImageUri != null) {
                    TextButton(onClick = { selectedImageUri = null }) { Text("Quitar") }
                }
            }
            selectedImageUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Previsualización",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = priceText, onValueChange = { priceText = it },
                label = { Text("Precio (CLP)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Tipo de producto
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = !typeExpanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = when (type) {
                        ProductType.LANA -> "Textil"
                        ProductType.MADERA -> "Madera"
                        ProductType.CERAMICA -> "Cerámica"
                        ProductType.GREDA -> "Greda"
                        ProductType.HILO -> "Hilo"
                        ProductType.PINTURA -> "Pintura"
                        ProductType.OTRO -> "Otro"
                    },
                    onValueChange = {},
                    label = { Text("Tipo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(typeExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    ProductType.entries.forEach { t ->
                        DropdownMenuItem(
                            text = { Text(t.name.lowercase().replaceFirstChar { it.titlecase() }) },
                            onClick = { type = t; typeExpanded = false }
                        )
                    }
                }
            }

            // Región (dropdown con regiones de Chile)
            ExposedDropdownMenuBox(
                expanded = regionExpanded,
                onExpandedChange = { regionExpanded = !regionExpanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = region,
                    onValueChange = {},
                    label = { Text("Región") },
                    placeholder = { Text("Selecciona…") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(regionExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = regionExpanded,
                    onDismissRequest = { regionExpanded = false }
                ) {
                    REGIONES_CHILE.forEach { r ->
                        DropdownMenuItem(
                            text = { Text(r) },
                            onClick = {
                                region = r
                                regionExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = stockText, onValueChange = { stockText = it },
                label = { Text("Stock") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = author, onValueChange = { author = it },
                label = { Text("Autor/Artesano") }, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            )

            Button(
                onClick = {
                    val product = NewProduct(
                        name = name.trim(),
                        price = priceText.toDoubleOrNull() ?: 0.0,
                        type = type,
                        region = region.trim(),
                        stock = stockText.toIntOrNull() ?: 1,
                        imageUri = selectedImageUri,
                        author = author.trim(),
                        description = description.trim()
                    )
                    onPublish(product)
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Publicar") }
        }
    }
}


