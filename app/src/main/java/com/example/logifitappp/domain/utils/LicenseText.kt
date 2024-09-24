package com.example.logifitappp.domain.utils


import androidx.compose.runtime.Composable
import com.example.logifitappp.R

@Composable
fun getLicenseInfo(license: String?): LicenseInfo {
    val (icon, label) = when (license?.lowercase()) {
        "logifit pro" -> Pair(R.drawable.ic_star, "PRO")
        "logifit premium" -> Pair(R.drawable.ic_crown, "PREMIUM")
        else -> Pair(R.drawable.ic_lightning, "LITE")
    }

    return LicenseInfo(icon, label)
}

data class LicenseInfo(
    val icon: Int,
    val label: String
)