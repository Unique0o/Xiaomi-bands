package com.example.logifitappp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    error = Rose120,
    errorContainer = Orange170,
    onErrorContainer = Rose120,
    onPrimary = White,
    onPrimaryContainer = Blue690,
    onSecondaryContainer = Green298,
    onSurface = White,
    onSurfaceVariant = Slate105,
    onTertiaryContainer = Orange390,
    outline = White,
    outlineVariant = Blue690,
    primary = Blue690,
    primaryContainer = Blue130,
    secondaryContainer = Lime70,
    surface = Zinc940,
    surfaceContainer = Slate705,
    surfaceContainerLowest = Lime30,
    surfaceTint = Stone470,
    tertiaryContainer = Amber60,
)

private val LightColorScheme = lightColorScheme(
    error = Rose120,
    errorContainer = Orange170,
    onErrorContainer = Rose120,
    onPrimary = White,
    onPrimaryContainer = Blue690,
    onSecondaryContainer = Green298,
    onSurface = Zinc940,
    onSurfaceVariant = Zinc680,
    onTertiaryContainer = Orange390,
    outline = Stone240,
    outlineVariant = Blue690,
    primary = Blue690,
    primaryContainer = Blue130,
    secondaryContainer = Lime70,
    surface = Lime30,
    surfaceContainer = White,
    surfaceContainerLowest = Lime30,
    surfaceTint = Stone470,
    tertiaryContainer = Amber60,
)

@Composable
fun LogifitApppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}