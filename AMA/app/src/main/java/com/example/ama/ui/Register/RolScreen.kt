package com.example.ama.ui.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.PrimaryButton
import com.example.ama.ui.navigation.Routes

// Route: rolScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolScreen(navController: NavController) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { }   // sin barra roja, todo va en el contenido
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // FLECHA ATRÁS ARRIBA A LA IZQUIERDA
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // LOGO CENTRADO (igual a la maqueta)
            Image(
                painter = painterResource(R.drawable.logo_artemayor_horizontal),
                contentDescription = "Arte Mayor",
                modifier = Modifier
                    .height(80.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(250.dp))

            // BOTÓN AMARILLO: COMPRADOR
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
                    text = "Comprador",
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Spacer(Modifier.height(24.dp))

            // BOTÓN ROJO: ARTESANO/A VENDEDOR
            PrimaryButton(
                text = "Artesano/a vendedor",
                onClick = { navController.navigate(Routes.REGISTER) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}
