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
import androidx.compose.material.icons.filled.Check

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.components.home.ShareButton
import com.example.logifitappp.ui.theme.LogifitApppTheme
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.res.stringResource

@Composable
fun SmartBandScreen(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(horizontal = 1.dp, vertical = 1.dp),

        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBEFF5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(14.dp)
        ) {
            SmartBandHeader()
            BatteryStatus(
                title = stringResource(id = R.string.battery_status),
                color = Color(0xFF2196F3)
            )
            WarningMessage(
                title = stringResource(id = R.string.warning_title),
                color = Color.LightGray,

            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusCard(stringResource(id = R.string.face_status), true)
                StatusCard(stringResource(id = R.string.face_dream), false)
            }

        }
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
            Text(stringResource(id = R.string.title_smart_band), fontWeight = FontWeight.Bold)
        }
        ShareButton(
            title = stringResource(id = R.string.share),
            color = Color.White,
            modifier = Modifier
                .height(30.dp)
                .width(122.dp),
            onClick = { /*TODO*/ })
    }
}

@Composable
fun BatteryStatus(
    title: String,
    modifier: Modifier = Modifier,
    color: Color,

    ) {
    Row(
        modifier = modifier
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
            Text(title, color = color)
        }
        ConnectedIndicator("CONECTADO", Color(0xFF4CAF50))
    }
}

@Composable
fun WarningMessage(
    title: String,
    modifier: Modifier = Modifier,
    color: Color
) {
    Row(
        modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            modifier = Modifier.weight(1f),
            color = color
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_upward),
            contentDescription = null,
            tint = Color.White,
            modifier = modifier
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
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(stringResource(id = R.string.title_tests), fontWeight = FontWeight.Bold)
            Text(
                stringResource(id = R.string.description_tests),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .background(Color(0xFF4CAF50), CircleShape)
                .size(40.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun TestsSomnolenciaCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
                Text(
                    text = "Mis tests de somnolencia",
//                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { /* Acción para agregar test */ },
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF4CAF50), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar test",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = "Realiza un test de somnolencia para verificar si estás apto o no para realizar tus labores.",
//                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "TEST DE AUTOEVALUACIÓN DE FATIGA",
                            color = Color(0xFF2196F3),
//                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "APTO",
                                color = Color(0xFF4CAF50),
//                                style = MaterialTheme.typography.caption,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Resultado de 24/10/2023",
//                        style = MaterialTheme.typography.caption,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )
                    ShareButton(
                        title = "Compartir",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { /*TODO*/ })
                }
            }
        }
    }
}

@Composable
fun TestFatigaItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBEFF5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(id = R.string.title_test_fatiga),
                    color = Color(0xFF2196F3),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                ConnectedIndicator(
                    text = stringResource(id = R.string.status_person),
                    color = Color(0xFF4CAF50)
                )

            }
            Text(
                text = stringResource(id = R.string.result_date),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
            )
            ShareButton(
                title = stringResource(id = R.string.share),
                color = Color.White,
                modifier = Modifier
                    .height(30.dp)
                    .width(122.dp),
                onClick = { /*TODO*/ })
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