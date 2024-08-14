package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime30
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.Orange390
import com.example.logifitappp.ui.theme.Stone240


@Composable
fun AlarmClockCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Stone240),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_alarm),
                        contentDescription = stringResource(id = R.string.content_description_alarm),
                        tint = Blue690
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.wake_up),
                        style = MaterialTheme.typography.labelSmall,
                        color = Blue690
                    )
                }
                ConnectedIndicator(
                    text = "20 min",
                    color = Green298,
                    backgroundColor = Lime70,
                    pointColor = Green298
                )
            }

            AlarmRow(duration = "5min", startTime = "02:14", endTime = "02:19")
            AlarmRow(duration = "15min", startTime = "03:54", endTime = "04:09")

        }
    }
}

@Composable
fun AlarmRow(
    duration: String,
    startTime: String,
    endTime: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = duration,
                style = MaterialTheme.typography.bodySmall,

                )
        }
        ProgressBar(color = Orange390)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = startTime,
                style = MaterialTheme.typography.bodySmall,

                )
            Text(
                text = endTime,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

    }
}
