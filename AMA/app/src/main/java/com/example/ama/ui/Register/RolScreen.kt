package com.example.ama.ui.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.components.PrimaryButton
import com.example.ama.ui.navigation.Routes

//ROute: rolScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RolScreen(navController: NavController) {
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
                text = "COMPRADOR",
                onClick = { navController.navigate(Routes.HOME) },
                modifier = Modifier.padding(vertical = 6.dp)
            )
            Spacer(Modifier.height(30.dp))

            PrimaryButton(
                text = "ARTESANO/A VENDEDOR",
                onClick = { navController.navigate(Routes.HOME) },
                modifier = Modifier.padding(vertical = 6.dp)
            )
            Spacer(Modifier.height(30.dp))

           /* PrimaryButton(
                text = "Entrar como invitado",
                onClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.START) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.padding(vertical = 6.dp)
            )

            Spacer(Modifier.height(24.dp))*/
        }
    }
}