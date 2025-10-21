package com.example.ama.ui.Login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.ama.R

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onSubmit: suspend () -> Boolean,          // el contenedor decide navegación
) {
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )

        // Campos
        val vm = androidx.lifecycle.viewmodel.compose.viewModel<LoginViewModel>()
        //Recuadro correo:
        OutlinedTextField(
            value = vm.email,
            onValueChange = { vm.onEmailChange(it) },
            label = { Text("Correo electrónico") },
            placeholder = { Text("ejemplo@gmail.com") },
            singleLine = true,
            isError = vm.emailError != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        //Error al ingresar correo
        AnimatedVisibility(vm.emailError != null) {
            Text(
                vm.emailError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        //Recuadro ingreso contraseña:
        OutlinedTextField(
            value = vm.password,
            onValueChange = { vm.onPasswordChange(it) },
            label = { Text("Contraseña") },
            singleLine = true,
            isError = vm.passwordError != null,
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Intento de submit con tecla Done
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        //Error contraseña:
        AnimatedVisibility(vm.passwordError != null) {
            Text(
                vm.passwordError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        //Se ordenan horizontalmente 3 elementos:
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = vm.rememberMe,
                onCheckedChange = { vm.toggleRememberMe() }
            )
            Text("Recordarme")
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { /* TODO: recuperar contraseña */ }) {
                Text("¿Olvidaste tu contraseña?")
            }
        }
        //Cierre Row

        // Botón Ingresar
        val scope = rememberCoroutineScope()
        Button(
            onClick = {
                scope.launch {
                    val ok = onSubmit()
                    if (!ok) {
                        snackbarHostState.showSnackbar(
                            "Revisa los campos e intenta nuevamente."
                        )
                    }
                }
            },
            enabled = !vm.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (vm.isLoading) {
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text("Ingresar")
        }

//        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 2.dp,
            color = Color.Gray
        )


        //        Llamamos a la fun para crear un boton personalizado con la img de facebook:
        CustomButton(Modifier.clickable{/*URL que sea*/}, painterResource(R.drawable.facebook), "FACEBOOK")

        Spacer(modifier = Modifier.height(8.dp))

        //        Llamamos a la fun para crear un boton personalizado con la img de google:
        CustomButton(Modifier.clickable{/*URL que sea*/}, painterResource(id = R.drawable.google), "GOOGLE")


    }
}

//fun composable para botones personalizados con iconos de google y facebook:
@Composable
fun CustomButton(modifier: Modifier, paint: Painter, title: String) {
    Box(
        modifier = modifier //parametro que recibe el modificador
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 32.dp)
            .background(MaterialTheme.colorScheme.primary, shape = CircleShape) //Color de fondo del boton
            .border(2.dp, Color.White, shape = CircleShape) //Borde del boton
        ,
        contentAlignment = Alignment.CenterStart
        //El texto siempre estara centrado aunque se ponga una img o no al boton al inicio
    )
    {
        Image(
            painter = paint, //parametro que recibe la img
            contentDescription = "Google",
            modifier = Modifier
                .padding(start = 24.dp)
                .size(24.dp)
        )


        Text(
            text = title, //parametro que recibe el texto
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold

        )
    }
}