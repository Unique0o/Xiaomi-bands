package com.example.logifitappp.ui.screens.wearable_detection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.ui.components.Text

@Composable
fun WearableDetectionListItem(
    candidate: WearableCandidate,
    onCandidatePressed: (candidate: WearableCandidate) -> Unit
) {
    Row(
        modifier = Modifier.clickable { onCandidatePressed(candidate) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            contentDescription = null,
            imageVector = Icons.Outlined.Watch,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                color = MaterialTheme.colorScheme.surfaceTint,
                text = candidate.getName(),
                typography = MaterialTheme.typography.titleLarge
            )

            Text(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = candidate.getMacAddress(),
                typography = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}