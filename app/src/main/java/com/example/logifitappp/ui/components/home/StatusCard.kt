package com.example.logifitappp.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120

@Composable
fun StatusCard(title: String, isApt: Boolean) {
    val backgroundColor = if (isApt) Lime70 else Orange170
    val iconColor = if (isApt) Green298 else Rose120
    val statusText = if (isApt) "APTO" else "NO APTO"
    val statusColor = if (isApt) Green298 else Rose120

    Card(
        modifier = Modifier
            .width(150.dp)
            .height(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Icon(
                painter = painterResource(
                    id = if (isApt) R.drawable.ic_sentiment_satisfied
                    else R.drawable.ic_sentiment_dissatisfied
                ),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(62.dp)
            )

            ConnectedIndicator(
                text = statusText,
                color = statusColor,
                backgroundColor = backgroundColor,
                pointColor = statusColor
            )
        }
    }
}