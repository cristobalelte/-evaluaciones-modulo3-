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
import androidx.compose.material.icons.filled.Star
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController
) {
    val registerVM: RegisterViewModel = viewModel()

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
                value = registerVM.name,
                onValueChange = {registerVM.onNameChange(it) },
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
                value = registerVM.lastName,
                onValueChange = {registerVM.onLastNameChange(it) },
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
                value = registerVM.email,
                onValueChange = {registerVM.onEmailChange(it) },
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
                value = registerVM.phone,
                onValueChange = {registerVM.onPhoneChange(it) },
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
                value = registerVM.password,
                onValueChange = {registerVM.onPasswordChange(it) },
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
                value = registerVM.confirmPassword,
                onValueChange = {registerVM.onConfirmPasswordChange(it) },
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
                    navController.navigate(Routes.HOME)
                    val text = "Registrando"
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
                    text = "REGISTRARME"
                )
            }

        }


    }
}

