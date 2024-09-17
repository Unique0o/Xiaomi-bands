package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.theme.Blue690

@Composable
fun HeaderRow(
    date: String,
    title: String,
    firstAlternativeTitle: String? = null,
    secondAlternativeTitle: String? = null,
    firstAlternativeSubtitle: String? = null,
    secondAlternativeSubtitle: String? = null,
    subColor: Color = Blue690
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
                onClick = { /*TODO*/ },
                horizontalPadding=  0.dp,
                verticalPadding=  0.dp,
                text = "",
                backgroundColor = MaterialTheme.colorScheme.surface,
                textColor = Color.Gray,
                modifier = Modifier.size(40.dp),
                cornerRadius = 10.dp,
                elevation = FloatingActionButtonDefaults.elevation( 0.dp )
            )
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.inverseSurface
            )
            IconButton(
                icon = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                onClick = { /*TODO*/ },
                horizontalPadding=  0.dp,
                verticalPadding=  0.dp,
                text = "",
                backgroundColor = MaterialTheme.colorScheme.surface,
                textColor = Color.Gray,
                modifier = Modifier.size(40.dp),
                cornerRadius = 10.dp,
                elevation = FloatingActionButtonDefaults.elevation( 0.dp )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.inverseSurface
        )
        if (firstAlternativeTitle != null || secondAlternativeTitle != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                firstAlternativeTitle?.let {
                    Text(text = it, style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Bold)
                }
//                secondAlternativeTitle?.let {
//                    Text(text = it, style = MaterialTheme.typography.labelMedium,
//                        color = MaterialTheme.colorScheme.onTertiaryContainer,
//                        fontWeight = FontWeight.Bold)
//                }
            }
        }

        if (firstAlternativeSubtitle != null || secondAlternativeSubtitle != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                firstAlternativeSubtitle?.let {
                    Text(text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = subColor
                    )
                }
                secondAlternativeSubtitle?.let {
                    Text(text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = subColor)
                }
            }
        }

    }
}

@Preview
@Composable
fun HeaderRowPreview() {
    HeaderRow(date = "Noviembre 20, 2023", title = "My Heart Rate")
}