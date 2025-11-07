package com.dominio.bloommind.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Gray,
    onPrimary = Black,
    background = Black,
    surface = DarkGray,
    onBackground = White,
    onSurface = White,
    error = ErrorRed,
    onError = White,
    surfaceTint = DarkGray
)

private val LightColorScheme = lightColorScheme(
    primary = Black, // For NavBar background
    onPrimary = White, // For NavBar icons
    background = White,
    surface = White, // For surfaces like cards and the gender field
    onBackground = Black,
    onSurface = Black,
    error = ErrorRed,
    onError = White,
    surfaceTint = White // This will remove the gray tint from cards
)

@Composable
fun BloomMindTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Dynamic color is disabled to enforce grayscale
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
