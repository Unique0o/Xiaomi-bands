package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@Composable
fun StepGraphCard(
    title: String,
    calories: Int,
    data: List<Float>,
    barColor: Color = Color(0xFFE8F5E9),
    accentColor: Color = Color(0xFF4CAF50),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Header(title, calories, accentColor)
            Spacer(modifier = Modifier.height(16.dp))
            StepGraph(data, barColor, accentColor)
            Spacer(modifier = Modifier.height(8.dp))
            TimeLabels()
        }
    }
}

@Composable
private fun Header(title: String, calories: Int, accentColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${calories}kcal",
            color = accentColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {

            Spacer(modifier = Modifier.width(8.dp))
            ConnectedIndicator(
                text = stringResource(id = R.string.without_data),
                color = Color.LightGray,
                backgroundColor = Color.LightGray
            )
        }
    }
}

@Composable
private fun StepGraph(data: List<Float>, barColor: Color, accentColor: Color) {
    val maxValue = data.maxOrNull() ?: 1f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { value ->
            Bar(
                height = (value / maxValue),
                barColor = barColor,
                accentColor = accentColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Bar(height: Float, barColor: Color, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 1.dp)
    ) {
        // Barra de fondo (vacía)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(barColor)
        )
//        // Barra de progreso
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(height)
//                .align(Alignment.BottomCenter)
//                .background(accentColor)
//        )
    }
}

@Composable
private fun TimeLabels() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "00:00", fontSize = 12.sp, color = Color.Gray)
        Text(text = "24:00", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun MyScreen() {
    val stepData = List(24) { kotlin.random.Random.nextFloat() }
    StepGraphCard(
        title = "Mis pasos",
        calories = 0,
        data = stepData,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
@Preview(showBackground = true)
fun MyScreenPreview() {
    MyScreen()
}