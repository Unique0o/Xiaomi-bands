package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.viewmodel.views.GraphicsViewModel

@Composable
fun GraphicsSentMessage(
    graphicsViewModel: GraphicsViewModel
) {
    val message = when {
        graphicsViewModel.state.drowsiness == null || graphicsViewModel.state.drowsiness!!.sentAt == null -> stringResource(R.string.sync_with_logifit_required_message)
        else -> "${stringResource(R.string.last_sending_to_logifit)}: ${DateTimeUtils.parse(graphicsViewModel.state.drowsiness!!.sentAt!!, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy h:mm a")}"
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = message,
            typography = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
            horizontalPadding = 10.dp,
            icon = Icons.AutoMirrored.Filled.Send,
            iconSize = 10.dp,
            modifier = Modifier.height(24.dp),
            text = stringResource(id = R.string.button_send),
            onClick = { graphicsViewModel.state.wearable?.let { graphicsViewModel.sendSleep(it) } },
            verticalPadding = 0.dp,
        )
    }
}