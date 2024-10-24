package com.example.logifitappp.enums

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Orange510

enum class LicenseTypeEnum(val color: Color, val icon: ImageVector, @StringRes val label: Int) {
    LITE(Orange510, Icons.Default.Bolt, R.string.lite),
    PREMIUM(Blue690, Icons.Default.AreaChart, R.string.premium),
    PRO(Green298, Icons.Default.Star, R.string.pro);
}