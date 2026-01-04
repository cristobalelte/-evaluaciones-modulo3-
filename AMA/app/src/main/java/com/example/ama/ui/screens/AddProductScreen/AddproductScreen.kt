package com.example.ama.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.ama.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.TopBar
import com.example.ama.ui.navigation.Routes
import com.example.ama.ui.screens.AddProductScreen.AddProductUiState
import com.example.ama.ui.screens.AddProductScreen.AddProductViewModel
import com.example.ama.ui.screens.AddProductScreen.PublishForm
import com.example.ama.ui.screens.AddProductScreen.PublishStep
import com.example.ama.ui.screens.Step2Images

// 🎨 Colores del Figma
private val FIGMA_FILL = Color(0xFF995340)
private val FIGMA_TRACK = Color(0x40D6A99D)
private val FIGMA_VOLVER = Color(0xFFAD0D14)
private val FIGMA_BG = Color(0xFFF9F9F9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    vm: AddProductViewModel,
    onBack: () -> Unit
) {
    val ui by vm.uiState.collectAsState()
    val form by vm.form.collectAsState()
    val ctx = LocalContext.current

    val supportsPhotoPicker = ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(ctx)
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        vm.setImageUri(uri)
    }
    val legacy = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        vm.setImageUri(uri)
    }

    fun pickImage() {
        if (supportsPhotoPicker) {
            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            legacy.launch("image/*")
        }
    }

    Scaffold(
        containerColor = FIGMA_BG,
        topBar = {
            TopBar(
                navController = navController,
                cartCount = 0,
                onOpenCart = { navController.navigate(Routes.CART) }
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                onHelpClick = { navController.navigate("help") },
                onPublishClick = {},
                onProfileClick = { navController.navigate("perfil") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(FIGMA_BG)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Publica tu artesanía",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge
            )

            PublishProgressFigma(step = ui.step)

            when (ui.step) {
                PublishStep.STEP_1 -> Step1Figma(
                    ui = ui,
                    form = form,
                    vm = vm,
                    onBack = { if (ui.step == PublishStep.STEP_1) onBack() else vm.back() },
                    onNext = { vm.next() }
                )
                PublishStep.STEP_2 -> Step2Images(
                    form = form,
                    onBack = { vm.back() },
                    onNext = { vm.next() },
                    onImageSelected = { uri -> vm.setImageUri(uri) }
                )
                PublishStep.STEP_3 -> Step3Figma(form = form, onPick = { pickImage() })
            }

            ui.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            if (ui.success && ui.createdProductId != null) {
                Text("✅ Publicado (id: ${ui.createdProductId})", color = Color(0xFF2E7D32))
            }




        }
    }
}

// ------------------------------------------------------------
// 🔹 Barra de progreso
// ------------------------------------------------------------
@Composable
fun PublishProgressFigma(step: PublishStep) {
    val current = when (step) {
        PublishStep.STEP_1 -> 1
        PublishStep.STEP_2 -> 2
        PublishStep.STEP_3 -> 3
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Paso $current de 3",
            color = FIGMA_FILL,
            style = MaterialTheme.typography.labelMedium
        )
        LinearProgressIndicator(
            progress = current / 3f,
            color = FIGMA_FILL,
            trackColor = FIGMA_TRACK,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(top = 4.dp)
        )
    }
}


// ------------------------------------------------------------
// 🔹 Paso 1
// ------------------------------------------------------------
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Figma(
    ui: AddProductUiState,
    form: PublishForm,
    vm: AddProductViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val brown = Color(0xFF995340)
    val grayBorder = Color(0xFFBDBDBD)
    val redButton = Color(0xFFAD0D14)
    val green = Color(0xFF4CAF50)

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

        Text(
            "Descripción del producto",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )

        // ---------- Nombre ----------
        Text("¿Cuál es el nombre de tu artesanía?*")
        OutlinedTextField(
            value = form.name,
            onValueChange = { vm.setName(it) },
            placeholder = { Text("Ej: Manta de lana fina color gris claro", color = Color(0xFF9E9E9E)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (ui.isValidName) Icon(Icons.Default.CheckCircle, null, tint = green)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (ui.isValidName) green else brown,
                unfocusedBorderColor = if (ui.isValidName) green else grayBorder,
                cursorColor = brown
            )
        )

        // ---------- Categoría ----------
        Text("Categoría*")
        var catExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = catExpanded, onExpandedChange = { catExpanded = !catExpanded }) {
            OutlinedTextField(
                value = ui.selectedCategory?.name ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Selecciona una categoría", color = Color(0xFF9E9E9E)) },
                trailingIcon = {
                    if (ui.isValidCategory)
                        Icon(Icons.Default.CheckCircle, null, tint = green)
                    else
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (ui.isValidCategory) green else brown,
                    unfocusedBorderColor = if (ui.isValidCategory) green else grayBorder,
                    cursorColor = brown
                )
            )
            ExposedDropdownMenu(expanded = catExpanded, onDismissRequest = { catExpanded = false }) {
                ui.categories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat.name ?: "") },
                        onClick = {
                            vm.onCategorySelected(cat)
                            catExpanded = false
                        }
                    )
                }
            }
        }

        // ---------- Subcategoría ----------
        Text("Subcategoría*")
        var subExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = subExpanded, onExpandedChange = { subExpanded = !subExpanded }) {
            OutlinedTextField(
                value = ui.selectedSubcategory?.name ?: "",
                onValueChange = {},
                readOnly = true,
                enabled = ui.subcategories.isNotEmpty(),
                placeholder = { Text("Selecciona una subcategoría", color = Color(0xFF9E9E9E)) },
                trailingIcon = {
                    if (ui.isValidSubcategory)
                        Icon(Icons.Default.CheckCircle, null, tint = green)
                    else
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = subExpanded)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (ui.isValidSubcategory) green else brown,
                    unfocusedBorderColor = if (ui.isValidSubcategory) green else grayBorder,
                    cursorColor = brown
                )
            )
            ExposedDropdownMenu(expanded = subExpanded, onDismissRequest = { subExpanded = false }) {
                ui.subcategories.forEach { sub ->
                    DropdownMenuItem(
                        text = { Text(sub.name ?: "") },
                        onClick = {
                            vm.onSubcategorySelected(sub)
                            subExpanded = false
                        }
                    )
                }
            }
        }

        // ---------- Tamaño ----------
        Text("Tamaño*")
        OutlinedTextField(
            value = form.size,
            onValueChange = { vm.setSize(it) },
            placeholder = { Text("Ej: 2 Plazas", color = Color(0xFF9E9E9E)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (ui.isValidSize) Icon(Icons.Default.CheckCircle, null, tint = green)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (ui.isValidSize) green else brown,
                unfocusedBorderColor = if (ui.isValidSize) green else grayBorder,
                cursorColor = brown
            )
        )

        // ---------- Color ----------
        Text("Color*")
        OutlinedTextField(
            value = form.color,
            onValueChange = { vm.setColor(it) },
            placeholder = { Text("Ej: Gris", color = Color(0xFF9E9E9E)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (ui.isValidColor) Icon(Icons.Default.CheckCircle, null, tint = green)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (ui.isValidColor) green else brown,
                unfocusedBorderColor = if (ui.isValidColor) green else grayBorder,
                cursorColor = brown
            )
        )

        // ---------- Precio ----------
        Text("Precio*")
        OutlinedTextField(
            value = form.price,
            onValueChange = { vm.setPrice(it) },
            placeholder = { Text("Ej: $54.000", color = Color(0xFF9E9E9E)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            trailingIcon = {
                if (ui.isValidPrice) Icon(Icons.Default.CheckCircle, null, tint = green)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (ui.isValidPrice) green else brown,
                unfocusedBorderColor = if (ui.isValidPrice) green else grayBorder,
                cursorColor = brown
            )
        )

        // ---------- Descripción ----------
        Text("Describe tu artesanía*")
        OutlinedTextField(
            value = form.description,
            onValueChange = { vm.setDescription(it) },
            placeholder = {
                Text("Describe materiales, técnica y qué la hace especial.", color = Color(0xFF9E9E9E))
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (ui.isValidDescription) green else brown,
                unfocusedBorderColor = if (ui.isValidDescription) green else grayBorder,
                cursorColor = brown

            )
        )
        Spacer(Modifier.height(6.dp))


// ---------- Botones ----------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp), // 🔹 solo pequeño espacio del campo anterior
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                border = BorderStroke(1.dp, redButton),
                shape = RoundedCornerShape(12.dp), // igual al borde de los campos
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp), // 🔹 altura similar al TextField
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = redButton,
                    containerColor = Color.White
                )
            ) {
                Text(
                    "Volver",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Button(
                onClick = onNext,
                enabled = vm.canContinueStep1(),
                shape = RoundedCornerShape(12.dp), // igual al borde de los campos
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp), // 🔹 misma altura
                colors = ButtonDefaults.buttonColors(
                    containerColor = redButton,
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFBDBDBD),
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    "Continuar",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }


    }
}





// ------------------------------------------------------------
// 🔹 Paso 2
// ------------------------------------------------------------
@Composable
fun Step2Images(
    form: PublishForm,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onImageSelected: (Uri) -> Unit
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    val grayBorder = Color(0xFFBDBDBD)
    val redButton = Color(0xFFAD0D14)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Fotos del producto",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        // 📸 Imagen principal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, grayBorder, RoundedCornerShape(12.dp))
                .clickable { imagePicker.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (form.imageUri != null) {
                // ✅ Imagen cargada
                AsyncImage(
                    model = form.imageUri,
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Ícono de check verde
                Image(
                    painter = painterResource(id = R.drawable.ic_check_circle),
                    contentDescription = "Imagen cargada correctamente",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                )

                // Texto confirmación
                Text(
                    text = "Tu foto se cargó correctamente.",
                    color = Color(0xFF4CAF50),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    fontSize = 12.sp
                )
            } else {
                // Estado vacío
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cloud_upload),
                        contentDescription = "Subir foto",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Haz click aquí para subir tu foto",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ➕ Miniaturas adicionales
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, grayBorder, RoundedCornerShape(8.dp))
                        .clickable { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Agregar foto adicional",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // 🔘 Botones inferiores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                border = BorderStroke(1.dp, redButton),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = redButton),
                modifier = Modifier.weight(1f)
            ) {
                Text("Volver")
            }

            Button(
                onClick = onNext,
                enabled = form.imageUri != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = redButton,
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFBDBDBD)
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Publicar")
            }
        }
    }
}



// ------------------------------------------------------------
// 🔹 Paso 3
// ------------------------------------------------------------
@Composable
private fun Step3Figma(
    form: PublishForm,
    onPick: () -> Unit
) {
    val brown = Color(0xFF995340)
    val grayBorder = Color(0xFFBDBDBD)

    Text(
        text = "Fotos del producto",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    OutlinedButton(
        onClick = onPick,
        border = BorderStroke(1.dp, grayBorder),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = brown)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = brown)
            Text("Haz click aquí para subir tu foto", color = brown)
        }
    }

    Spacer(Modifier.height(12.dp))

    form.imageUri?.let { uri ->
        AsyncImage(
            model = uri,
            contentDescription = "foto",
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(top = 4.dp)
        )
    }
}

// ------------------------------------------------------------
// 🔹 Footer
// ------------------------------------------------------------
@Composable
private fun FooterFigma(
    step: PublishStep,
    loading: Boolean,
    canContinue: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onPublish: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onBack,
            border = BorderStroke(1.dp, FIGMA_VOLVER),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = FIGMA_VOLVER,
                containerColor = Color.White
            ),
            modifier = Modifier.height(48.dp),
            enabled = !loading
        ) { Text("Volver", color = FIGMA_VOLVER) }

        val isLast = step == PublishStep.STEP_3
        Button(
            onClick = if (isLast) onPublish else onNext,
            modifier = Modifier.height(48.dp),
            enabled = !loading && (canContinue || isLast),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLast) FIGMA_FILL else Color(0xFFBDBDBD),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFE0E0E0)
            )
        ) {
            if (loading && isLast) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(if (isLast) "Publicar" else "Continuar")
            }
        }
    }
}
