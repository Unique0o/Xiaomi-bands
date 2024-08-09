package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R

@Composable
fun SleepInfoCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bed),
                    contentDescription = "Sleep",
                    tint = Color(0xFF4285F4)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.information_sleep),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4285F4)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SleepTypeInfo(
                    color = Color(0xFF8AB4F8),
                    type = stringResource(id = R.string.light_sleep),
                    percentage = "89%",
                    duration = "5h 20min",
                    isColumn = true
                )
                SleepTypeInfo(
                    color = Color(0xFF4285F4),
                    type = stringResource(id = R.string.deep_sleep),
                    percentage = "4%",
                    duration = "50min",
                    isColumn = true
                )
            }

            SleepTypeInfo(
                color = Color(0xFF7E57C2),
                type = stringResource(id = R.string.rem_sleep),
                percentage = "7%",
                duration = "1h 26min",
                isColumn = false
            )
        }
    }
}

@Composable
fun SleepTypeInfo(
    color: Color,
    type: String,
    percentage: String,
    duration: String,
    isColumn: Boolean
) {
    val content = @Composable {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "$type ($percentage)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
            Text(
                text = duration,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (isColumn) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                content()
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}