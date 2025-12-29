package com.example.ama.ui.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.navigation.Routes
import com.example.ama.ui.theme.onPrimaryLight
import com.example.ama.ui.theme.primaryLight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onLoggedIn: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val vm: LoginViewModel = viewModel()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(primaryLight)
                    .padding(horizontal = 16.dp),
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = onPrimaryLight
                    )
                }

                Image(
                    painter = painterResource(R.drawable.logo_artemayor_blanco),
                    contentDescription = "Arte Mayor",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(56.dp)
                        .clickable { navController.navigate(Routes.HOME) },
                    contentScale = ContentScale.Fit
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LoginForm(
            modifier = Modifier.padding(padding),
            snackbarHostState = snackbarHostState,
            vm = vm,
            onSubmit = {
                val ok = vm.login(context) // o vm.login() si tu función no recibe context
                if (ok) {
                    scope.launch { snackbarHostState.showSnackbar("¡Bienvenido!") }
                    onLoggedIn()
                }
                ok
            }
        )
    }
}

