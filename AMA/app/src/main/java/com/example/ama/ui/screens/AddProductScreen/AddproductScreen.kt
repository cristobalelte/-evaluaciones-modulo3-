package com.example.ama.ui.screens.AddProductScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ama.ui.components.BottomBar
import com.example.ama.ui.components.TopBar
import androidx.navigation.NavController
import com.example.ama.core.dto.PublishForm
import com.example.ama.core.dto.PublishStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    cartCount: Int,
    onOpenCart: () -> Unit,
    vm: AddProductViewModel,
    onBack: () -> Unit
) {
    val state by vm.ui.collectAsState()
    val ctx = LocalContext.current

    val supportsPhotoPicker = ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(ctx)
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        vm.setImage(uri)
    }
    val legacy = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        vm.setImage(uri)
    }

    fun pickImage() {
        if (supportsPhotoPicker) {
            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            legacy.launch("image/*")
        }
    }

    Scaffold(
        containerColor = Color.White, // <-- fondo blanco figma
        topBar = {
            TopBar(
                navController = navController,
                cartCount = cartCount,
                onOpenCart = onOpenCart
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                onHelpClick = { navController.navigate("help") }, // si lo tienes
                onPublishClick = { /* ya estás aquí */ },
                onProfileClick = { navController.navigate("perfil") } // si lo tienes
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {

            // Título como figma: "Publica tu artesanía"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
            ) {
                TextButton(onClick = onBack, contentPadding = PaddingValues(0.dp)) { Text("←") }
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Publica tu artesanía",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            PublishProgressFigma(step = state.step)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (state.step) {
                    PublishStep.STEP_1 -> Step1Figma(form = state.form, onChange = vm::update)
                    PublishStep.STEP_2 -> Step2Figma(form = state.form, onChange = vm::update)
                    PublishStep.STEP_3 -> Step3DoneFigma(createdName = state.created?.name)
                }

                state.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(8.dp))
            }

            FooterFigma(
                step = state.step,
                loading = state.loading,
                onBack = { vm.back().also { if (state.step == PublishStep.STEP_1) onBack() } },
                onNext = { vm.next() },
                onPublish = { vm.publish() }
            )
        }
    }
}

@Composable
private fun PublishProgressFigma(step: PublishStep) {
    val (current, total) = when (step) {
        PublishStep.STEP_1 -> 1 to 3
        PublishStep.STEP_2 -> 2 to 3
        PublishStep.STEP_3 -> 3 to 3
    }

    Column(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Text("Paso $current de $total", style = MaterialTheme.typography.labelMedium, color = Color(0xFF7A7A7A))
        Spacer(Modifier.height(8.dp))

        // barra estilo figma (progreso marrón + fondo claro)
        LinearProgressIndicator(
            progress = current / total.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Color(0xFF8B4A3B),
            trackColor = Color(0xFFE9E2F6)
        )
    }
}

@Composable
private fun Step1Figma(
    form: PublishForm,
    onChange: ((PublishForm) -> PublishForm) -> Unit
) {
    Text("Descripción del producto", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

    FigmaField(
        label = "¿Cuál es el nombre de tu artesanía?*",
        value = form.name,
        placeholder = "Ej: Collar artesanal de piedras",
        onValueChange = { v -> onChange { current -> current.copy(name = v) } }
    )

    FigmaField(
        label = "Categoría* (ID)",
        value = form.categoryId,
        placeholder = "Selecciona una categoría",
        keyboardType = KeyboardType.Number,
        onValueChange = { v -> onChange { it.copy(categoryId = v.filter(Char::isDigit)) } }
    )

    // Si aún no tienes endpoint de categorías/subcategorías, lo dejamos como texto por ahora.
    FigmaField(
        label = "Subcategoría*",
        value = form.material, // si en tu backend “material” no es subcat, cambia esto luego
        placeholder = "Selecciona una subcategoría",
        onValueChange = { v -> onChange { it.copy(material = v) } }
    )

    FigmaField(
        label = "Tamaño*",
        value = form.size,
        placeholder = "Selecciona un tamaño",
        onValueChange = { v -> onChange { it.copy(size = v) } }
    )

    FigmaField(
        label = "Color*",
        value = form.color,
        placeholder = "Selecciona un color",
        onValueChange = { v -> onChange { it.copy(color = v) } }
    )

    FigmaField(
        label = "Precio*",
        value = form.price,
        placeholder = "Escribe el valor de tu artesanía",
        keyboardType = KeyboardType.Number,
        onValueChange = { v -> onChange { it.copy(price = v.filter(Char::isDigit)) } }
    )

    FigmaTextArea(
        label = "Describe tu artesanía*",
        value = form.description,
        placeholder = "Describe tu artesanía: materiales, técnica y qué la hace especial.",
        onValueChange = { v -> onChange { it.copy(description = v) } }
    )
}

@Composable
private fun Step2Figma(
    form: PublishForm,
    onChange: ((PublishForm) -> PublishForm) -> Unit
) {
    Text("Fotos del producto", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

    // Caja grande + texto como figma
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 0.dp,
        color = Color.White,
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Haz click aquí para subir tu foto", color = Color(0xFF7A7A7A))
        }
    }

    Spacer(Modifier.height(8.dp))

    form.imageUri?.let { uri ->
        AsyncImage(
            model = uri,
            contentDescription = "foto",
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )
    }
}

@Composable
private fun Step3DoneFigma(createdName: String?) {
    // Modal/éxito figma: simple (puedes después convertirlo a Dialog)
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("¡Tu artesanía se ha publicado con éxito!", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(createdName ?: "")
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { /* seguir editando */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBA3F))
            ) { Text("Seguir editando", color = Color.Black) }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(onClick = { /* ir al producto */ }) {
                Text("Ir al producto")
            }
        }
    }
}

@Composable
private fun FooterFigma(
    step: PublishStep,
    loading: Boolean,
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
            modifier = Modifier.weight(1f),
            enabled = !loading,
            shape = RoundedCornerShape(16.dp)
        ) { Text("Volver", color = Color(0xFF8B0000)) }

        when (step) {
            PublishStep.STEP_1, PublishStep.STEP_2 -> {
                Button(
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    enabled = !loading,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDBDBD))
                ) { Text("Continuar", color = Color.White) }
            }
            PublishStep.STEP_3 -> {
                Button(
                    onClick = onPublish,
                    modifier = Modifier.weight(1f),
                    enabled = !loading,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (loading) CircularProgressIndicator(Modifier.size(18.dp))
                    else Text("Publicar")
                }
            }
        }
    }
}

@Composable
private fun FigmaField(
    label: String,
    value: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Text(label, color = Color(0xFF7A7A7A), style = MaterialTheme.typography.labelMedium)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFBDBDBD),
            unfocusedBorderColor = Color(0xFFBDBDBD)
        )
    )
}

@Composable
private fun FigmaTextArea(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Text(label, color = Color(0xFF7A7A7A), style = MaterialTheme.typography.labelMedium)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFBDBDBD),
            unfocusedBorderColor = Color(0xFFBDBDBD)
        )
    )
}
