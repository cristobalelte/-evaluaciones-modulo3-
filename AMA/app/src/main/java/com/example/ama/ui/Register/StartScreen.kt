package com.example.ama.ui.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.PrimaryButton
import com.example.ama.ui.navigation.Routes
import androidx.compose.foundation.BorderStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(navController: NavController) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {}
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(R.drawable.logo_artemayor_horizontal),
                contentDescription = "Arte Mayor",
                modifier = Modifier.height(80.dp)
            )

            Spacer(Modifier.height(32.dp))

            // ============= BOTÓN ROJO: INICIAR SESIÓN =============
            PrimaryButton(
                text = "Iniciar sesión",
                onClick = { navController.navigate(Routes.LOGIN) },
                modifier = Modifier.padding(vertical = 6.dp)
            )

            Spacer(Modifier.height(16.dp))

            // ============= BOTÓN AMARILLO: REGISTRARSE =============
            Button(
                onClick = { navController.navigate(Routes.REGISTER) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107),  // Amarillo
                    contentColor = Color(0xFF7B001A)     // Rojo Arte Mayor
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "Registrarse",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(Modifier.height(16.dp))

            // ============= BOTÓN BORDE ROJO: INVITADO =============
            OutlinedButton(
                onClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.START) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF7B001A)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF7B001A)
                )
            ) {
                Text(
                    text = "Entrar como invitado",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
