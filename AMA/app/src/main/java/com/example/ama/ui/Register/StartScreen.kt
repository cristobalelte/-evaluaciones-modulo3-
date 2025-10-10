package com.example.ama.ui.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.navigation.Routes

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.outline,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(navController: NavController) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {}) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            Image(
                painter = painterResource(R.drawable.logo_artemayor_horizontal),
                contentDescription = "Arte Mayor",
                modifier = Modifier.height(80.dp)
            )

            Spacer(Modifier.height(30.dp))

            PrimaryButton(
                text = "Iniciar Sesión",
                onClick = { navController.navigate(Routes.LOGIN) },
                modifier = Modifier.padding(vertical = 6.dp)
            )
            Spacer(Modifier.height(30.dp))

            PrimaryButton(
                text = "Registrarse",
                onClick = { navController.navigate(Routes.REGISTER) },
                modifier = Modifier.padding(vertical = 6.dp)
            )
            Spacer(Modifier.height(30.dp))

            PrimaryButton(
                text = "Entrar como invitado",
                onClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.START) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.padding(vertical = 6.dp)
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}