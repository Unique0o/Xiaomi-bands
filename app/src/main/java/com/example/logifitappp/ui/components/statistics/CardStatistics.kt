package com.example.logifitappp.ui.components.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Blue690

@Composable
fun CardStatistics() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pie_chart),
                contentDescription = null,
                tint = Blue690,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(46.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total" ,
                    fontWeight = FontWeight.Bold,

                    )
                Text(
                    text = "12",

                    )
            }
            Spacer(modifier = Modifier.width(44.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.status_no_aptos),
                    fontWeight = FontWeight.Bold,

                    )
                Text(
                    text = "3",

                    )
            }
            Spacer(modifier = Modifier.width(42.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.status_aptos),
                    fontWeight = FontWeight.Bold,

                    )
                Text(
                    text = "7",

                    )
            }
            Spacer(modifier = Modifier.width(44.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text ="S/D" ,
                    fontWeight = FontWeight.Bold,

                    )
                Text(
                    text = "2",

                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardStatisticsPreview() {
    CardStatistics()
}