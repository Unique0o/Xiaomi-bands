package com.example.logifitappp.ui.screens.wearable_detection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.ui.components.Text

@Composable
fun WearableDetectionList(
    candidates: SnapshotStateList<WearableCandidate>,
    onCandidatePressed: (candidate: WearableCandidate) -> Unit
) {
    LazyColumn {
        items(candidates) {candidate ->
            Row(
                modifier = Modifier.clickable { onCandidatePressed(candidate) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Watch,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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
    }
}