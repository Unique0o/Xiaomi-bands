package com.example.logifitappp.ui.screens.lesson_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.data.remote.dto.response.FetchLessonResponse
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.theme.Gray615
import com.example.logifitappp.ui.theme.Green298

@Composable
fun LessonDetailFooter(
    modifier: Modifier = Modifier,
    lesson: FetchLessonResponse?,
    markAsCompleted: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth().then(modifier),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {  }
        ) {
            Icon(
                contentDescription = "prev lesson",
                imageVector = Icons.Default.ChevronLeft,
                modifier = Modifier.size(24.dp),
                tint = Gray615
            )
        }

        Spacer(Modifier.width(2.dp))

        if (lesson?.isCompleted == true) {
            Spacer(Modifier.weight(1f))

            IconText(
                icon = Icons.Default.CheckCircle,
                iconColor = Green298,
                iconSize = 24.dp,
                label = stringResource(R.string.completed),
                labelColor = Green298
            )

            Spacer(Modifier.weight(1f))
        } else {
            Button(
                modifier = Modifier.weight(1f),
                onClick = markAsCompleted,
                text = stringResource(R.string.button_mark_as_view)
            )
        }

        Spacer(Modifier.width(2.dp))

        IconButton(
            onClick = {  }
        ) {
            Icon(
                contentDescription = "next lesson",
                imageVector = Icons.Default.ChevronRight,
                modifier = Modifier.size(24.dp),
                tint = Gray615
            )
        }
    }
}