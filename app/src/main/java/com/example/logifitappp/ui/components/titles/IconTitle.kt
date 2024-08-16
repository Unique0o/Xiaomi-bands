package com.example.logifitappp.ui.components.titles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun IconTitle(
    horizontalPadding: Dp = 24.dp,
    icon: ImageVector,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.inverseSurface
) {
    Row(
        modifier = Modifier.padding(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor
        )

        Text(
            color = textColor,
            modifier = Modifier.padding(start = 3.dp),
            text = text,
            typography = MaterialTheme.typography.displayLarge
        )
    }

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
