package com.example.logifitappp.ui.components.graphics

import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime30
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.Stone470
import com.example.logifitappp.ui.theme.White

@Composable
fun StepEmptyGraphCard(
    title: String,
    data: List<Float>,
    barColor: Color,
    accentColor: Color,
    backgroundColorConnect: Color,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Header(title, accentColor, backgroundColorConnect, iconResId)
            Spacer(modifier = Modifier.height(16.dp))
            StepGraph(data, barColor, accentColor)
            Spacer(modifier = Modifier.height(8.dp))
            TimeLabels()
        }
    }
}

@Composable
private fun Header(
    title: String,
    accentColor: Color,
    backgroundColorConnect: Color,
    @DrawableRes iconResId: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "Calories icon",
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                color = accentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {

            Spacer(modifier = Modifier.width(8.dp))
            ConnectedIndicator(
                text = stringResource(id = R.string.without_data),
                color = Stone470,
                backgroundColor = backgroundColorConnect,
                pointColor = Stone470
            )
        }
    }
}

@Composable
private fun StepGraph(
    data: List<Float>,
    barColor: Color, accentColor: Color,
) {
    val maxValue = data.maxOrNull() ?: 1f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
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
        Text(text = "00:00", fontSize = 12.sp, color = Stone470)
        Text(text = "24:00", fontSize = 12.sp, color = Stone470)
    }
}

@Composable
fun MyScreen() {
    val stepData = List(24) { kotlin.random.Random.nextFloat() }
    StepEmptyGraphCard(
        title = "Mis pasos",
        data = stepData,
        modifier = Modifier.padding(16.dp),
        iconResId = R.drawable.ic_fire,
        backgroundColorConnect = Lime30,
        barColor =Lime70,
        accentColor = Green298
    )
}

@Composable
@Preview(showBackground = true)
fun MyScreenPreview() {
    MyScreen()
}