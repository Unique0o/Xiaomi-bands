package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Lime30
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.theme.Stone240
import com.example.logifitappp.ui.theme.Stone470
import com.example.logifitappp.ui.theme.Zinc680

@Composable
fun EmptyInfoGraph(
    title: String,
    timeRange: String,
    backgroundColor: Color = Stone240,
    textColor:  Color = Blue690,
    accentColor: Color = Blue690,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.outline
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Header(title, timeRange, textColor, accentColor)
            Spacer(modifier = Modifier.height(16.dp))
            EmptyBars()
        }
    }
}

@Composable
private fun Header(
    title: String,
    timeRange: String,
    textColor: Color,
    accentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh icon",
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$title $timeRange",
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        ConnectedIndicator(text = stringResource(id = R.string.without_data),
            color = Zinc680,
            backgroundColor = Lime30,
            pointColor = Zinc680,
        )
    }
}

@Composable
private fun EmptyBars() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 2.dp)
                    .background(Stone470)
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun EmptyInfoGraphPreview() {
    LogifitApppTheme {
        EmptyInfoGraph(
            title = "Información entre",
            timeRange = "19:00 - 07:00",
            modifier = Modifier.padding(16.dp)
        )
    }

}
@Composable
@Preview()
fun EmptyInfoGraphDarkPreview() {
    LogifitApppTheme(darkTheme = true) {
        EmptyInfoGraph(
            title = "Información entre",
            timeRange = "19:00 - 07:00",
            modifier = Modifier.padding(16.dp)
        )
    }

}