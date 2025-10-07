package com.example.ama.ui.Register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.navigation.Routes

data class NewRegister(
    val name: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegScreenDClass(
    navController: NavController,
    onPublish: (NewRegister) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    fun onEmailChange(v: String) {
        email = v
        validateEmail(v) // 👈 validación inmediata al escribir
    }

    var password by remember { mutableStateOf("") }
    fun onPasswordChange(v: String) {
        password = v
        validatePassword(v) // 👈 validación inmediata al escribir
    }
    var confirmPassword by remember { mutableStateOf("") }
    fun onConfirmPasswordChange(v: String) {
        confirmPassword = v
        validateConfirmPassword(password, v) // 👈
    }




    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_artemayor_horizontal),
                        contentDescription = "Arte Mayor",
                        alignment = Alignment.Center
                    )
                }
                /*       navigationIcon = {
                           IconButton(onClick = onOpenSettings) {
                               Icon(Icons.Outlined.Settings, contentDescription = "Configuración")
                           }
                       },
                       actions = {
                           IconButton(onClick = onOpenCart) {
                               BadgedBox(badge = { if (cartCount > 0) Badge { Text("$cartCount") } }) {
                                   Icon(Icons.Outlined.ShoppingCart, contentDescription = "Carrito")
                               }
                           }
                       }*/
            )
        }
    )
    { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
//                Arrangment = Arrangement.Center,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                text = "Ingresa tus datos"
            )

            Spacer(
                Modifier.height(4.dp)
            )

//            FORMULARIO:

            Row {
                Text("NOMBRE")
                Icon(
                    imageVector = Icons.Default.StarOutline,
                    contentDescription = "Campo obligatorio"
                )
            }


            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Ingresa tu nombre aqui") },
                placeholder = { Text("") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )

            Row {
                Text("APELLIDO")
                Icon(
                    imageVector = Icons.Default.StarOutline,
                    contentDescription = "Campo obligatorio"
                )
            }


            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Ingresa tu apellido aqui") },
                placeholder = { Text("") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )

            Row {
                Text("CORREO")
                Icon(
                    imageVector = Icons.Default.StarOutline,
                    contentDescription = "Campo obligatorio"
                )
            }

            TextField(
                value = email,
                onValueChange = { onEmailChange(it) },
                label = { Text("Ingresa tu correo aqui") },
                placeholder = { Text("") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )

            Row {
                Text("TELEFONO DE CONTACTO")
                Icon(
                    imageVector = Icons.Default.StarOutline,
                    contentDescription = "Campo obligatorio"
                )
            }

            TextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Ingresa tu numero de telefono aqui") },
                placeholder = { Text("") },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )

            Row {
                Text("CONTRASEÑA")
                Icon(
                    imageVector = Icons.Default.StarOutline,
                    contentDescription = "Campo obligatorio"
                )
            }
            var showPassword by remember { mutableStateOf(false) }

            TextField(
                value = password,
                onValueChange = { onPasswordChange(it) },
                label = { Text("Ingresa tu contraseña aqui") },
                placeholder = { Text("") },
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
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )

            Row {
                Text("VUELVE A INGRESAR TU CONTRASEÑA")
                Icon(
                    imageVector = Icons.Default.StarOutline,
                    contentDescription = "Campo obligatorio"
                )
            }

            TextField(
                value = confirmPassword,
                onValueChange = { onConfirmPasswordChange(it) },
                label = { Text("Ingresa nuevamente tu contraseña aqui") },
                placeholder = { Text("") },
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
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Spacer(
                Modifier.height(4.dp)
            )



            Button(
                onClick = {
                    if (emailError != null || passwordError != null || confirmPasswordError != null) {
                        val register = NewRegister(
                            name = name.trim(),
                            lastName = lastName.trim(),
                            phone = phone.trim(),
                            email = email.trim(),
                            password = password.trim(),
                            confirmPassword = confirmPassword.trim()
                        )
//                    Agregamos al nuevo usuario al registro:
                        onPublish(register)
                        Toast.makeText(
                            navController.context,
                            "Registro exitoso",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(Routes.HOME)
                    }
                    else{
                        Toast.makeText(
                            navController.context,
                            "Registro fallido",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

//                    navController.navigate(Routes.HOME)
                },
//                    enabled = items.isNotEmpty(),
                //Cambio de shape btn proceder al pago:
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    text = "REGISTRARME"
                )
            }

        }


    }

}


// Valida correo */
var emailError: String? by mutableStateOf(null)
private fun validateEmail(value: String) {
    emailError =
        if (value.isBlank()) "El correo no puede estar vacío"
        else if (!Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$").matches(value))
            "Debe ser un correo Gmail válido (ejemplo@gmail.com)"
        else null
}

//Validar password:
var passwordError: String? by mutableStateOf(null)

private fun validatePassword(value: String){
    passwordError =
        if (value.isBlank()) "La contraseña no puede estar vacía"
        else if (value.length < 8) "La contraseña debe tener al menos 8 caracteres"
        else null
}

var confirmPasswordError: String? by mutableStateOf(null)
private fun validateConfirmPassword(password: String, confirmPassword: String){
    confirmPasswordError =
    if(password != confirmPassword) ("Las contraseñas no coinciden")
    else null
}
