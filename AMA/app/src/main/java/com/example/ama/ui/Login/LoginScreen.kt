package com.example.ama.ui.Login

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val vm = androidx.lifecycle.viewmodel.compose.viewModel<LoginViewModel>()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("AMA") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LoginForm(
            modifier = Modifier.padding(padding),
            snackbarHostState = snackbarHostState,
            onSubmit = {
                val ok = vm.login()
                if (ok) {
                    scope.launch { snackbarHostState.showSnackbar("¡Bienvenido!") }
                    onLoggedIn()
                }
                ok
            }
        )
    }
}
