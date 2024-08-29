package com.example.logifitappp.ui.components.statistics

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.CardLayout
import com.example.logifitappp.ui.components.home.ConnectedIndicator


@Composable
fun MyTeamCard(
    name: String,
    shift: String,
    statusIndicator: String,
    textIndicatorColor: Color,
    backgroundIndicatorColor: Color,
    pointIndicatorColor: Color
) {
    CardLayout(
        //icon = painterResource(id = R.drawable._135715_1),
        iconSize = 20.dp,
        label = name,
        labelStyle = TextStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        ),
        cardBackgroundColor = MaterialTheme.colorScheme.surfaceContainer,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        Textcolor = Color.Gray,
        suffixComponent = {
            ConnectedIndicator(
                text = statusIndicator,
                color = textIndicatorColor,
                backgroundColor = backgroundIndicatorColor,
                pointColor = pointIndicatorColor
            )
        },
        bodyComponent = {
            Row {
                Text(
                    text = stringResource(id = R.string.shift) +": ",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = shift,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
    )
}