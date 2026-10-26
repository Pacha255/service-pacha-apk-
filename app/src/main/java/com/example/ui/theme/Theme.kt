package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = PachaOrange,
    onPrimary = Color.White,
    primaryContainer = PachaOrangeSoft,
    onPrimaryContainer = Color.Black,
    secondary = PachaOrange,
    onSecondary = Color.White,
    secondaryContainer = PachaOrangeSoft,
    onSecondaryContainer = Color.Black,
    tertiary = PachaGold,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = PachaGoldDark,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF9FAFB),
    onSurfaceVariant = Color(0xFF374151),
    outline = PachaBorder
)

// Force light theme with white and orange (no dark blue/black backgrounds) to strictly respect white background and black text
private val DarkColorScheme = lightColorScheme(
    primary = PachaOrange,
    onPrimary = Color.White,
    primaryContainer = PachaOrangeSoft,
    onPrimaryContainer = Color.Black,
    secondary = PachaOrange,
    onSecondary = Color.White,
    secondaryContainer = PachaOrangeSoft,
    onSecondaryContainer = Color.Black,
    tertiary = PachaGold,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = PachaGoldDark,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF9FAFB),
    onSurfaceVariant = Color(0xFF374151),
    outline = PachaBorder
)

@Composable
fun ServicePachaTheme(
    darkTheme: Boolean = false, // Force light theme with white background and black text
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
