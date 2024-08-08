package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.components.home.ShareButton
import com.example.logifitappp.ui.theme.LightGray
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun SmartBandScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        SmartBandHeader()
        BatteryStatus()
        WarningMessage()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusCard("SUEÑO", true)
            StatusCard("FATIGA", false)
        }
        TestsSection()
    }
}

@Composable
fun SmartBandHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_watch),
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Mi Smart Band 4", fontWeight = FontWeight.Bold)
        }
//        Button(
//            onClick = { /* TODO */ },
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
//            shape = RoundedCornerShape(16.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.Share,
//                modifier = Modifier.size(24.dp),
//                contentDescription = null,
//                tint = Color.White
//            )
//            Text("Compartir", color = Color.White)

//        }
        ShareButton()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatteryStatus() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_battery_full),
                contentDescription = null,
                tint = Color(0xFF2196F3)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Batería al 80%", color = Color(0xFF2196F3))
        }
        ConnectedIndicator("CONECTADO", Color(0xFF4CAF50))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarningMessage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Cuidado!!: No te encuentras apto para conducir con las condiciones actuales.",
            modifier = Modifier.weight(1f),
            color = Color.LightGray
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_upward),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF4CAF50), CircleShape)
                .padding(8.dp)
        )
    }
}

@Composable
//fun StatusCard(title: String, status: String, isApt: Boolean) {
//    Card(
//        modifier = Modifier
//            .width(150.dp)
//            .height(120.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(title, fontWeight = FontWeight.Bold)
//            Icon(
//                painter = painterResource(
//                    id = if (isApt) R.drawable.ic_sentiment_satisfied
//                    else R.drawable.ic_sentiment_dissatisfied
//                ),
//                contentDescription = null,
//                tint = if (isApt) Color(0xFF4CAF50) else Color(0xFFF44336),
//                modifier = Modifier.size(48.dp)
//            )
//            Text(
//                status,
//                color = if (isApt) Color(0xFF4CAF50) else Color(0xFFF44336),
//                modifier = Modifier.background(Color(0xFFE8F5E9), RoundedCornerShape(40.dp)),
//                fontSize = 12.sp
//            )
//        }
//    }
//}
fun StatusCard(title: String, isApt: Boolean) {
    val backgroundColor = if (isApt) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val iconColor = if (isApt) Color(0xFF4CAF50) else Color(0xFFF44336)
    val statusText = if (isApt) "APTO" else "NO APTO"
    val statusColor = if (isApt) Color(0xFF4CAF50) else Color(0xFFF44336)

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                modifier = Modifier.size(64.dp)
            )
            Box(
                modifier = Modifier
                    .background(backgroundColor, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    statusText,
                    color = statusColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun TestsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_assignment),
            contentDescription = null,
            tint = Color.Black
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)) {
            Text("Mis tests de somnolencia", fontWeight = FontWeight.Bold)
            Text(
                "Realiza un test de somnolencia para verificar si estás apto o no para realizar tus labores.",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .background(Color(0xFF2196F3), CircleShape)
                .size(40.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LogifitApppTheme {
        SmartBandScreen()
    }
}