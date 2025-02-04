package com.example.logifitappp.core.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.Color

object ColorUtils {
    fun getGradientColor(@ColorInt colors: IntArray): Int {
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors)

        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        gradientDrawable.setBounds(0, 0, canvas.width, canvas.height)
        gradientDrawable.draw(canvas)

        return bitmap.getPixel(0, 0)
    }

    fun toColor(hex: String): Color {
        try {
            val cleanHex = hex.removePrefix("#")

            val red = cleanHex.substring(0, 2).toInt(16) / 255f
            val green = cleanHex.substring(2, 4).toInt(16) / 255f
            val blue = cleanHex.substring(4, 6).toInt(16) / 255f

            return Color(red, green, blue, 1f)
        } catch (e: Exception) {
            return Color.Black
        }
    }

    fun toHex(color: Color): String {
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()

        return String.format("#%02X%02X%02X", red, green, blue)
    }
}