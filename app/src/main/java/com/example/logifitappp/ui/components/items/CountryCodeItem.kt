package com.example.logifitappp.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.data.models.CountryPhoneCode

@Composable
fun CountryCodeItem(
    country: CountryPhoneCode,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        country.flagResId?.let { flagResId ->
            Image(
                painter = painterResource(id = flagResId),
                contentDescription = "${country.name} flag",
                modifier = Modifier.size(24.dp)
            )
        }
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = country.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = country.code, style = MaterialTheme.typography.bodyMedium)
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}