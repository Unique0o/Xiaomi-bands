package com.example.logifitappp.ui.screens.lesson_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.data.remote.dto.response.FetchLessonResponse
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.Video

@Composable
fun LessonDetailHeader(
    lesson: FetchLessonResponse?,
    onEnd: () -> Unit
) {
    Video(
        onEnd = onEnd,
        uri = lesson?.video ?: ""
    )

    Spacer(Modifier.height(16.dp))

    Column(Modifier.padding(horizontal = 16.dp)) {
        IconText(
            icon = Icons.AutoMirrored.Filled.Segment,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 24.dp,
            label = stringResource(R.string.description),
            labelTypography = MaterialTheme.typography.displayMedium
        )

        Spacer(Modifier.height(6.dp))

        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = lesson?.description ?: "",
            typography = MaterialTheme.typography.labelMedium
        )
    }
}