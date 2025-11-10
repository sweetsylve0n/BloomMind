package com.dominio.bloommind.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


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
    primary = Black,
    onPrimary = White,
    background = White,
    surface = White,
    onBackground = Black,
    onSurface = Black,
    error = ErrorRed,
    onError = White,
    surfaceTint = White
)

@Composable
fun BloomMindTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
