package com.example.ama.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary            = primaryLight,
    onPrimary          = onPrimaryLight,
    primaryContainer   = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,

    secondary          = secondaryLight,
    onSecondary        = onSecondaryLight,

    background         = backgroundLight,
    onBackground       = onBackgroundLight,
    surface            = surfaceLight,
    onSurface          = onSurfaceLight,
    surfaceVariant     = surfaceVariantLight,
    onSurfaceVariant   = onSurfaceVariantLight,

    error              = errorLight,
    onError            = onErrorLight,
    outline            = outlineLight
)

private val DarkColors = darkColorScheme(
    primary            = primaryDark,
    onPrimary          = onPrimaryDark,
    primaryContainer   = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,

    secondary          = secondaryDark,
    onSecondary        = onSecondaryDark,

    background         = backgroundDark,
    onBackground       = onBackgroundDark,
    surface            = surfaceDark,
    onSurface          = onSurfaceDark,
    surfaceVariant     = surfaceVariantDark,
    onSurfaceVariant   = onSurfaceVariantDark,

    error              = errorDark,
    onError            = onErrorDark,
    outline            = outlineDark
)

/* ----------------------------------------------------------
   Tema: opción para usar Dynamic Color (Android 12+)
   ---------------------------------------------------------- */

@Composable
fun AMATheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = false, // ponlo en true si quieres Material You
    content: @Composable () -> Unit
) {
    val colors =
        if (useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        } else {
            if (darkTheme) DarkColors else LightColors
        }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShape,
        content = content
    )
}
