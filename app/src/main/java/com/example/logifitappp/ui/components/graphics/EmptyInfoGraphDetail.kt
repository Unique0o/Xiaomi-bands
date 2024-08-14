package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Stone470

@Composable
fun EmptyInfoGraphDetail(
    title: String,
    titleGraph: String,
    timeRange: String,
    backgroundColor: Color = Stone470,
    textColor: Color = Blue690
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Header(title, titleGraph, timeRange, textColor)
        Spacer(modifier = Modifier.height(16.dp))
        EmptyBars()
    }
}

@Composable
private fun Header(
    title: String,
    titleGraph: String,
    timeRange: String,
    textColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val date = "Noviembre 20, 2023"
        HeaderRow(date = date, title = title)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$titleGraph $timeRange",
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
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

@Preview(showBackground = true)
@Composable
fun EmptyInfoGraphDetailPreview() {
    EmptyInfoGraphDetail(
        title = "Tiempo de sueño",
        titleGraph = "Información entre",
        timeRange = "19:00 - 07:00"
    )
}
