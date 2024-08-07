package com.example.logifitappp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.noto_sans_regular)),
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),

    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.noto_sans_regular)),
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),

    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.noto_sans_regular)),
        fontSize = 10.sp,
        lineHeight = 12.sp,
    ),

    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.noto_sans_medium)),
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),

    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.noto_sans_bold)),
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),

    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.noto_sans_light)),
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),

    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.noto_sans_light)),
        fontSize = 10.sp,
        lineHeight = 12.sp,
    ),
)