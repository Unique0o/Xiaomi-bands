package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text


@Composable
fun MetricsCard(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),

        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
//                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E5B9A)
            )
            content()
        }
    }
}

@Composable
fun SleepMetricsCard() {
    MetricsCard(
        title = "Mi sueño",
        content = {
            SleepInfoRow(
                title = "Información entre 19:00 - 07:00",
                value = "7h 36min",
                valueColor = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(8.dp))
            SleepChart(
                startHour = 22,
                endHour = 6,
                sleepDuration = 456
            )
        }
    )
}

@Composable
fun SleepInfoRow(
    title: String,
    value: String,
    valueColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
//            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
        Text(
            text = value,
//            style = MaterialTheme.typography.body2,
            color = valueColor
        )
    }
}

@Composable
fun SleepChart(
    startHour: Int,
    endHour: Int,
    sleepDuration: Int
) {
    val totalHours = endHour - startHour
    val sleepHours = sleepDuration / 60
    val wakeHours = totalHours - sleepHours

    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(Color(0xFFE8F1FF), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(sleepHours.toFloat() / totalHours)
                    .fillMaxHeight()
                    .background(Color(0xFF4285F4))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .weight(wakeHours.toFloat() / totalHours)
                    .fillMaxHeight()
                    .background(Color(0xFFE8F1FF))
            )
        }
    }

}

@Composable
fun StepsMetricsCard() {
    MetricsCard(
        title = "Mis pasos",
        content = {
            StepsInfoRow(
                caloriesValue = "200kcal",
                stepsValue = "1200 pasos",
                stepsValueColor = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(8.dp))
            StepsChart(
                stepsCount = 1200
            )
        }
    )
}

@Composable
fun StepsInfoRow(
    caloriesValue: String,
    stepsValue: String,
    stepsValueColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = caloriesValue,
//            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
        Text(
            text = stepsValue,
//            style = MaterialTheme.typography.body2,
            color = stepsValueColor
        )
    }
}

@Composable
fun StepsChart(
    stepsCount: Int
) {
    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(Color(0xFFE8F1FF), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(5) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (it < stepsCount / 240) Color(0xFF4CAF50) else Color(
                                0xFFE8F1FF
                            )
                        )
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun HeartRateMetricsCard() {
    MetricsCard(
        title = "Mi ritmo cardiaco",
        content = {
            HeartRateInfoRow(
                lastMeasurementLabel = "Última medición registrada",
                lastMeasurementValue = "90 LPM",
                lastMeasurementValueColor = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(8.dp))
            HeartRateChart()
        }
    )
}

@Composable
fun HeartRateInfoRow(
    lastMeasurementLabel: String,
    lastMeasurementValue: String,
    lastMeasurementValueColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = lastMeasurementLabel,
//            style = MaterialTheme.typography.body2,
            color = Color.Gray
        )
        Text(
            text = lastMeasurementValue,
//            style = MaterialTheme.typography.body2,
            color = lastMeasurementValueColor
        )
    }
}

@Composable
fun HeartRateChart(
) {
    Box(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .background(Color(0xFFE8F1FF), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFF7043))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFF7043))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFF4CAF50))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFF7043))
            )
        }
    }
}