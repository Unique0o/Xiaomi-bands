package com.example.logifitappp.ui.screens.synchronization_report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.viewmodel.states.SynchronizationReportItemType

@Composable
fun SynchronizationReportTeam(
    items: List<SynchronizationReportItemType>?,
    onShareWeeklyReport: () -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconText(
                icon = Icons.Default.SupervisorAccount,
                iconColor = MaterialTheme.colorScheme.onSurface,
                iconSize = 24.dp,
                label = stringResource(R.string.team_title),
                labelTypography = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                horizontalPadding = 10.dp,
                icon = Icons.Default.Share,
                iconSize = 10.dp,
                modifier = Modifier.height(24.dp),
                text = stringResource(id = R.string.button_share),
                onClick = { onShareWeeklyReport() },
                verticalPadding = 0.dp,
            )
        }

        Spacer(Modifier.height(8.dp))

        items?.forEachIndexed { index, item ->
            SynchronizationReportTeamItem(item)

            if (index + 1 < items.size) {
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}