package com.example.logifitappp.ui.screens.spo2_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.enums.Spo2ModeEnum
import com.example.logifitappp.ui.components.graphics.ChartLegendItem

@Composable
fun Spo2DetailLegend() {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChartLegendItem(
            color = Color(Spo2ModeEnum.AWAKE.color),
            label = stringResource(R.string.awake_measurement_legend)
        )

        Spacer(Modifier.width(4.dp))

        ChartLegendItem(
            color = Color(Spo2ModeEnum.ASLEEP.color),
            label = stringResource(R.string.asleep_measurement_legend)
        )
    }
}