package com.example.ama.ui.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
    val vm = androidx.lifecycle.viewmodel.compose.viewModel<LoginViewModel>()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                expandedHeight = 100.dp,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryLight, //Color de fondo
                    titleContentColor = onPrimaryLight, //Color del texto
                    navigationIconContentColor = onPrimaryLight, // Color del icono de navegación
                    actionIconContentColor = onPrimaryLight
                ),
                navigationIcon = {
                    Image(
                        alignment = Alignment.Center,
                        painter = painterResource(R.drawable.logo_artemayor_blanco),
                        contentDescription = "Arte Mayor",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable(onClick = { navController.navigate(Routes.HOME) })
                            .width(120.dp),
                        contentScale = ContentScale.Fit // O usa ContentScale.Fit si prefieres
                    )
                },

                title = {
                    /* Image(
                         alignment = Alignment.CenterStart,
                         painter = painterResource(R.drawable.logo_artemayor_blanco),
                         contentDescription = "Arte Mayor",
                         modifier = Modifier
                             .clickable(onClick = { navController.navigate(Routes.HOME) })
                             .width(100.dp),
                         contentScale = ContentScale.Fit // O usa ContentScale.Fit si prefieres
                     )*/
                },
            )
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
