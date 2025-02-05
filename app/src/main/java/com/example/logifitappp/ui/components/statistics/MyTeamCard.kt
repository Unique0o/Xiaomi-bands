package com.example.logifitappp.ui.components.statistics

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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



@Composable
fun MyTeamCard(
    icon : Int,
    name: String,
    subtitleAlternateText: String,
    statusIndicator: String,
    textIndicatorColor: Color,
    backgroundIndicatorColor: Color,
    pointIndicatorColor: Color
) {
    CardLayout(
        icon = painterResource(icon),
        iconColor = Color.Unspecified,
        iconSize = 65.dp,
        label = name,
        labelStyle = TextStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        ),
        cardBackgroundColor = MaterialTheme.colorScheme.surfaceContainer,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        textcolor = Color.Gray,
        suffixComponent = {

        },
        titleAlternateText = stringResource(id = R.string.shift) +": ",
        subtitleAlternateText = subtitleAlternateText,
        bodyComponent = {
            /* to do */
        },
    )
}