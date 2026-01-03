package com.example.ama.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ama.R
import com.example.ama.ui.navigation.Routes

@Composable
fun BottomBar(
    navController: NavController,
    onHelpClick: () -> Unit = {},
    onPublishClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Surface(
        color = Color(0xFF606060),
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(86.dp)
                .padding(horizontal = 12.dp)
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItemPng(
                label = "Inicio",
                iconRes = R.drawable.ic_home, // TU PNG con círculo
                onClick = { navController.navigate(Routes.HOME) },
                iconSize = 40
            )

            BottomBarItemPngCircle(
                label = "Ayuda",
                iconRes = R.drawable.ic_help, // tu PNG
                onClick = onHelpClick,
                iconSize = 22,                // tamaño del ícono dentro del círculo
                circleSize = 44,              // tamaño del círculo
                circleColor = Color(0xFFFFBA3F) // #FFBA3F
            )

            BottomBarItemPng(
                label = "Publicar",
                iconRes = R.drawable.ic_add, // TU PNG con círculo
                onClick = {
                    navController.navigate(Routes.PUBLISH)
                    onPublishClick()
                },
                iconSize = 40
            )

            BottomBarItemPng(
                label = "Perfil",
                iconRes = R.drawable.ic_profile, // TU PNG con círculo
                onClick = onProfileClick,
                iconSize = 40
            )
        }
    }
}


@Composable
private fun BottomBarItemPng(
    label: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    iconSize: Int = 40, // ajusta aquí
) {
    Column(
        modifier = Modifier
            .width(78.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(iconSize.dp)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
private fun BottomBarItemPngCircle(
    label: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    iconSize: Int = 22,
    circleSize: Int = 44,
    circleColor: Color = Color(0xFFFFBA3F),
) {
    Column(
        modifier = Modifier
            .width(78.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(circleSize.dp)
                .clip(CircleShape)
                .background(circleColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = label,
                modifier = Modifier.size(iconSize.dp)
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

