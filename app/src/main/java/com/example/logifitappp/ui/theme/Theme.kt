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
    onSecondaryContainer = Orange390,
    onSurface = White,
    onSurfaceVariant = Slate105,
    onTertiaryContainer = Orange390,
    outline = Slate705,
    outlineVariant = Blue690,
    primary = Blue690,
    primaryContainer = Blue130,
    secondaryContainer = Lime30,
    surface = Zinc940,
    surfaceContainer = Slate705,
    surfaceContainerLowest = LightGray,
    surfaceTint = Stone470,
    tertiaryContainer = Amber60,
)

private val LightColorScheme = lightColorScheme(
    error = Rose120,
    errorContainer = Orange170,
    onErrorContainer = Rose120,
    onPrimary = White,
    onPrimaryContainer = Blue690,
    onSecondaryContainer = Blue690,
    onSurface = Zinc940,
    onSurfaceVariant = Zinc680,
    onTertiaryContainer = Zinc940,
    outline = Stone240,
    outlineVariant = Blue690,
    primary = Blue690,
    primaryContainer = Blue130,
    secondaryContainer = LightGray,
    surface = White,
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