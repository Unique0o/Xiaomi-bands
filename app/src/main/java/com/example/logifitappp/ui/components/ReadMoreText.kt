package com.example.logifitappp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.example.logifitappp.R

@Composable
fun ReadMoreText(
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = 3,
    text: String,
    typography: TextStyle = MaterialTheme.typography.bodyLarge
) {
    var isExpanded by remember { mutableStateOf(false) }
    var hasMoreLineInDescription by remember { mutableStateOf(false) }

    Column {
        Text(
            color = color,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
            onTextLayout = { result -> hasMoreLineInDescription = result.hasVisualOverflow },
            overflow = TextOverflow.Ellipsis,
            text = text,
            typography = typography
        )

        Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Link(
                text = stringResource(if (isExpanded) R.string.link_show_less else R.string.link_show_more)
            ) {
                isExpanded = !isExpanded
            }
        }
    }
}