package com.example.logifitappp.ui.components.titles

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.components.Text
import androidx.compose.ui.text.TextStyle

enum class IconPosition {
    LEADING,
    TRAILING
}

@Composable
fun IconTitle(
    icon: ImageVector,
    text: String,
    iconPosition: IconPosition = IconPosition.LEADING,
    iconColor: Color = MaterialTheme.colorScheme.inverseSurface,
    textColor: Color = MaterialTheme.colorScheme.inverseSurface,
    iconSize: Int = 24,
    textTypography: TextStyle = MaterialTheme.typography.displayLarge
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (iconPosition == IconPosition.LEADING) Arrangement.Start else Arrangement.End
    ) {
        if (iconPosition == IconPosition.LEADING) {
            IconView(icon, iconColor, iconSize)
            Spacer(modifier = Modifier.width(8.dp))
            TextView(text, textColor, textTypography)
        } else {
            TextView(text, textColor, textTypography)
            Spacer(modifier = Modifier.weight(1f))
            IconView(icon, iconColor, iconSize)
        }
    }
}

@Composable
private fun IconView(icon: ImageVector, iconColor: Color, iconSize: Int) {
    Image(
        imageVector = icon,
        contentDescription = null,
        colorFilter = ColorFilter.tint(iconColor),
        modifier = Modifier
            .size(iconSize.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun TextView(text: String, textColor: Color, textTypography: TextStyle) {
    Text(
        text = text,
        color = textColor,
        typography = textTypography
    )
}

@Preview
@Composable
fun PreviewIconTitle() {
    LogifitApppTheme {
        IconTitle(
            icon = ImageVector.vectorResource(id = R.drawable.ic_location),
            text = stringResource(id = R.string.title_my_test)
        )
    }
}
