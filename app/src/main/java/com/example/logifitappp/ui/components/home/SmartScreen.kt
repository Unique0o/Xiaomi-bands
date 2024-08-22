package com.example.logifitappp.ui.components.home

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
import com.example.logifitappp.ui.theme.LogifitApppTheme
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120
import com.example.logifitappp.ui.theme.Stone240
import com.example.logifitappp.ui.theme.Stone470
import com.example.logifitappp.ui.theme.White

@Composable
fun SmartBandScreen(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(335.dp)
            .padding(horizontal = 1.dp, vertical = 1.dp),

        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        SmartBandHeader()
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.outline)
                .padding(14.dp)
        ) {

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
                StatusCard(stringResource(id = R.string.face_dream), true)
                StatusCard(stringResource(id = R.string.face_status), false)
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
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                stringResource(id = R.string.title_smart_band),
                fontWeight = FontWeight.Bold
            )
        }
        ShareButton(
            title = stringResource(id = R.string.share),
            color = White,
            onClick = { /*TODO*/ }
        )

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
                painter = painterResource(id = R.drawable.ic_battery),
                contentDescription = null,
                tint = Blue690
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, color = color)
        }
        ConnectedIndicator(
            text = stringResource(id = R.string.connected),
            color = Green298,
            backgroundColor = Lime70,
            pointColor = Green298
        )
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
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            modifier = Modifier.weight(1f),
            color = color
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_cloud_upload),
            contentDescription = null,
            tint = White,
            modifier = modifier
                .size(40.dp)
                .background(Green298, CircleShape)
                .padding(8.dp)
        )
    }
}

@Composable
fun StatusCard(title: String, isApt: Boolean) {
    val backgroundColor = if (isApt) Lime70 else Orange170
    val iconColor = if (isApt) Green298 else Rose120
    val statusText = if (isApt) "APTO" else "NO APTO"
    val statusColor = if (isApt) Green298 else Rose120

    Card(
        modifier = Modifier
            .width(160.dp)
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

@Composable
fun TestsSection(buttonColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_test),
            contentDescription = null,
            modifier = Modifier .padding(bottom = 41.dp)
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
                color = Stone470
            )
        }
        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .background(buttonColor, CircleShape)
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
                    tint = Blue690
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
                        .background(Green298, CircleShape)
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
              style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 5.dp, bottom = 16.dp)
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
                            tint = Blue690,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "TEST DE AUTOEVALUACIÓN DE FATIGA",
                            color = Blue690,
//                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .background(Stone240, RoundedCornerShape(12.dp))
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
                        color = White,
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
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.outline),
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
                    tint = Blue690,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(id = R.string.title_test_fatiga),
                    color =Blue690,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 0.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                ConnectedIndicator(
                    text = stringResource(id = R.string.status_person),
                    color = Green298,
                    backgroundColor = Lime70,
                    pointColor = Green298
                )

            }
            Text(
                text = stringResource(id = R.string.result_date),
                style = MaterialTheme.typography.labelSmall,
                color = Stone470,
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
            )
            ShareButton(
                title = stringResource(id = R.string.share),
                color = White,
                modifier = Modifier
                    .height(40.dp)
                    .width(122.dp),
                onClick = { /*TODO*/ }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LogifitApppTheme {
        SmartBandScreen()
        //SmartBandHeader()
        //TestFatigaItem()
        //TestsSection( buttonColor = Green298)
        //TestsSomnolenciaCard()

    }
}