package com.example.ama.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.ama.R

// Provider de Google Fonts (ya lo tenías)
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Familia Lato (normal + bold)
private val latoFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Lato"),
        fontProvider = provider,
        weight = FontWeight.Normal
    ),
    Font(
        googleFont = GoogleFont("Lato"),
        fontProvider = provider,
        weight = FontWeight.Bold
    )
)

// Tipografía base de Material3
private val baseline = Typography()

// AppTypography usando Lato y ajustando:
// - headlineSmall = Heading S (24, bold, lh 24)
// - bodyMedium    = Body B-1 (16, regular, lh 21)
val AppTypography = Typography(
    displayLarge  = baseline.displayLarge.copy(fontFamily = latoFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = latoFontFamily),
    displaySmall  = baseline.displaySmall.copy(fontFamily = latoFontFamily),

    headlineLarge = baseline.headlineLarge.copy(fontFamily = latoFontFamily),
    headlineMedium= baseline.headlineMedium.copy(fontFamily = latoFontFamily),

    // Heading S
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = latoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    titleLarge    = baseline.titleLarge.copy(fontFamily = latoFontFamily),
    titleMedium   = baseline.titleMedium.copy(fontFamily = latoFontFamily),
    titleSmall    = baseline.titleSmall.copy(fontFamily = latoFontFamily),

    bodyLarge     = baseline.bodyLarge.copy(fontFamily = latoFontFamily),

    // Body B-1
    bodyMedium    = baseline.bodyMedium.copy(
        fontFamily = latoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.sp
    ),

    bodySmall     = baseline.bodySmall.copy(fontFamily = latoFontFamily),

    labelLarge    = baseline.labelLarge.copy(fontFamily = latoFontFamily),
    labelMedium   = baseline.labelMedium.copy(fontFamily = latoFontFamily),
    labelSmall    = baseline.labelSmall.copy(fontFamily = latoFontFamily),
)
