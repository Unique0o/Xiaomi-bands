package com.example.logifitappp.core.utils

import androidx.compose.ui.graphics.Color

object ColorUtils {
    fun toColor(hex: String): Color {
        val cleanHex = hex.removePrefix("#")

        val red = cleanHex.substring(0, 2).toInt(16) / 255f
        val green = cleanHex.substring(2, 4).toInt(16) / 255f
        val blue = cleanHex.substring(4, 6).toInt(16) / 255f

        return Color(red, green, blue, 1f)
    }

    fun toHex(color: Color): String {
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()

        return String.format("#%02X%02X%02X", red, green, blue)
    }
}