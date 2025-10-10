package com.example.ama.ui.theme
//package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.example.ama.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto Flex"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Noto Sans Thai"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

// Usa fuentes del sistema por ahora (evita provider + certs)
val AppTypography = Typography(
    displayLarge  = Typography().displayLarge.copy(fontFamily = FontFamily.SansSerif),
    displayMedium = Typography().displayMedium.copy(fontFamily = FontFamily.SansSerif),
    displaySmall  = Typography().displaySmall.copy(fontFamily = FontFamily.SansSerif),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = FontFamily.SansSerif),
    headlineMedium= Typography().headlineMedium.copy(fontFamily = FontFamily.SansSerif),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = FontFamily.SansSerif),
    titleLarge    = Typography().titleLarge.copy(fontFamily = FontFamily.SansSerif),
    titleMedium   = Typography().titleMedium.copy(fontFamily = FontFamily.SansSerif),
    titleSmall    = Typography().titleSmall.copy(fontFamily = FontFamily.SansSerif),
    bodyLarge     = Typography().bodyLarge.copy(fontFamily = FontFamily.SansSerif),
    bodyMedium    = Typography().bodyMedium.copy(fontFamily = FontFamily.SansSerif),
    bodySmall     = Typography().bodySmall.copy(fontFamily = FontFamily.SansSerif),
    labelLarge    = Typography().labelLarge.copy(fontFamily = FontFamily.SansSerif),
    labelMedium   = Typography().labelMedium.copy(fontFamily = FontFamily.SansSerif),
    labelSmall    = Typography().labelSmall.copy(fontFamily = FontFamily.SansSerif),
)

