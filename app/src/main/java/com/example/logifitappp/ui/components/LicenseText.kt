package com.example.logifitappp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.enums.LicenseTypeEnum

@Composable
fun LicenseText(
    license: String?
) {
    val licenseType = when (license?.lowercase()) {
        "logifit pro" -> LicenseTypeEnum.PRO
        "logifit premium" -> LicenseTypeEnum.PREMIUM
        else -> LicenseTypeEnum.LITE
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = licenseType.icon,
            contentDescription = null,
            tint = licenseType.color,
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = stringResource(id = licenseType.label).uppercase(),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            typography = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}