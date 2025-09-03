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

data class NewProduct(
    val name: String,
    val price: Double,
    val type: ProductType,
    val region: String,
    val stock: Int,
    val imageUri: Uri?,          // 👈 ahora guardamos el URI, no un String URL
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

    var type by remember { mutableStateOf(ProductType.TEXTIL) }
    var expanded by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Photo Picker (Android 13+ y dispositivos compatibles) + fallback GetContent
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

    val isValid = name.isNotBlank() && priceText.toDoubleOrNull() != null

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
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    readOnly = true,
                    value = when (type) {
                        ProductType.TEXTIL -> "Textil"
                        ProductType.MADERA -> "Madera"
                        ProductType.CERAMICA -> "Cerámica"
                        ProductType.GREDA -> "Greda"
                        ProductType.HILO -> "Hilo"
                        ProductType.PINTURA -> "Pintura"
                        ProductType.OTRO -> "Otro"
                    },
                    onValueChange = {},
                    label = { Text("Tipo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    ProductType.entries.forEach { t ->
                        DropdownMenuItem(
                            text = { Text(t.name.lowercase().replaceFirstChar { it.titlecase() }) },
                            onClick = { type = t; expanded = false }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = region, onValueChange = { region = it },
                label = { Text("Región") }, modifier = Modifier.fillMaxWidth()
            )

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
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp)
            )

            Button(
                onClick = {
                    val product = NewProduct(
                        name = name.trim(),
                        price = priceText.toDoubleOrNull() ?: 0.0,
                        type = type,
                        region = region.trim(),
                        stock = stockText.toIntOrNull() ?: 1,
                        imageUri = selectedImageUri,    // 👈 pasamos el URI elegido
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

