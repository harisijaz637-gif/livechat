package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PoppoColorScheme = darkColorScheme(
    primary = PoppoVioletPrimary,
    onPrimary = Color.White,
    primaryContainer = PoppoVioletDark,
    onPrimaryContainer = Color.White,
    secondary = PoppoPinkAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0x33FF2D85),
    onSecondaryContainer = PoppoPinkAccent,
    tertiary = PoppoGold,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0x33FFB300),
    onTertiaryContainer = PoppoGoldLight,
    background = PoppoBackground,
    onBackground = PoppoTextPrimary,
    surface = PoppoSurface,
    onSurface = PoppoTextPrimary,
    surfaceVariant = PoppoSurfaceVariant,
    onSurfaceVariant = PoppoTextSecondary,
    outline = PoppoSurfaceBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Poppo Live is an immersive dark/neon entertainment experience
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PoppoColorScheme,
        typography = Typography,
        content = content
    )
}
