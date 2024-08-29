package com.example.logifitappp.ui.screens.statistics


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.statistics.CardStatistics
import com.example.logifitappp.ui.components.statistics.DropdownShift
import com.example.logifitappp.ui.components.statistics.MyTeamCard
import com.example.logifitappp.ui.components.statistics.RangeDateSelect
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120
import com.example.logifitappp.ui.components.statistics.TitleIcon
import com.example.logifitappp.ui.theme.Stone240
import com.example.logifitappp.ui.theme.Stone470
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.theme.Blue690
import androidx.compose.runtime.*
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70

@Composable
fun SynchronizationReportScreen() {
    var showNoFitTeamCards by remember { mutableStateOf(true) }
    var showFitTeamCards by remember { mutableStateOf(false) }

    SimplePage(
        content = {
            Text(
                stringResource(id = R.string.my_statistics),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(15.dp))
            CardStatistics(
                total = "12",
                notFitTotal = "3",
                fitTotal = "7",
                sdTotal = "2"
            )
            IconText(
                Icons.Outlined.CheckCircle,
                text = stringResource(id = R.string.updated_information) + " " + "24/03/2023, 8.25AM"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RangeDateSelect(
                    label = "27/07/2023",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                DropdownShift(
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier.width(150.dp),
                    onClick = {
                        showNoFitTeamCards = true
                        showFitTeamCards = false
                    },
                    text = stringResource(id = R.string.status_no_apto),
                    containerColors = if(showNoFitTeamCards) Blue690 else Color.Transparent,
                    colorText = if(showNoFitTeamCards) MaterialTheme.colorScheme.onPrimary else Blue690
                )
                Spacer(modifier = Modifier.width(42.dp))
                Button(
                    modifier = Modifier.width(150.dp),
                    onClick = {
                        showNoFitTeamCards = false
                        showFitTeamCards = true
                    },
                    text = stringResource(id = R.string.status_person),
                    containerColors = if(showFitTeamCards) Blue690 else Color.Transparent,
                    colorText = if(showFitTeamCards) MaterialTheme.colorScheme.onPrimary else Blue690
                )
            }
            TitleIcon(title = stringResource(id = R.string.my_team), icon = Icons.Default.Share)
            if (showNoFitTeamCards) {
                MyTeamCard(
                    name = "Eduardo Palomino Cacéres",
                    subtitleAlternateText = "DIA",
                    statusIndicator = stringResource(id = R.string.status_no_apto),
                    textIndicatorColor = Rose120,
                    backgroundIndicatorColor = Orange170,
                    pointIndicatorColor = Rose120
                )
                MyTeamCard(
                    name = "Eduardo Palomino Cacéres",
                    subtitleAlternateText = "DIA",
                    statusIndicator = stringResource(id = R.string.status_no_apto),
                    textIndicatorColor = Rose120,
                    backgroundIndicatorColor = Orange170,
                    pointIndicatorColor = Rose120
                )
                MyTeamCard(
                    name = "Eduardo Palomino Cacéres",
                    subtitleAlternateText = "DIA",
                    statusIndicator = stringResource(id = R.string.status_no_apto),
                    textIndicatorColor = Rose120,
                    backgroundIndicatorColor = Orange170,
                    pointIndicatorColor = Rose120
                )
                MyTeamCard(
                    name = "Eduardo Palomino Cacéres",
                    subtitleAlternateText = "DIA",
                    statusIndicator = stringResource(id = R.string.without_data),
                    textIndicatorColor = Stone470,
                    backgroundIndicatorColor = Stone240,
                    pointIndicatorColor = Stone470
                )
            }
            if (showFitTeamCards) {
                MyTeamCard(
                    name = "Eduardo Palomino Cacéres",
                    subtitleAlternateText = "DIA",
                    statusIndicator = stringResource(id = R.string.status_person),
                    textIndicatorColor = Green298,
                    backgroundIndicatorColor = Lime70,
                    pointIndicatorColor = Green298
                )
                MyTeamCard(
                    name = "Eduardo Palomino Cacéres",
                    subtitleAlternateText = "DIA",
                    statusIndicator = stringResource(id = R.string.status_person),
                    textIndicatorColor = Green298,
                    backgroundIndicatorColor = Lime70,
                    pointIndicatorColor = Green298
                )
                MyTeamCard(
                    name = "Eduardo Palomino Cacéres",
                    subtitleAlternateText = "DIA",
                    statusIndicator = stringResource(id = R.string.status_person),
                    textIndicatorColor = Green298,
                    backgroundIndicatorColor = Lime70,
                    pointIndicatorColor = Green298
                )
                MyTeamCard(
                    name = "Eduardo Palomino Cacéres",
                    subtitleAlternateText = "DIA",
                    statusIndicator = stringResource(id = R.string.status_person),
                    textIndicatorColor = Green298,
                    backgroundIndicatorColor = Lime70,
                    pointIndicatorColor = Green298
                )
            }
        }
    )
}



@Composable
fun IconText(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier =  Modifier.padding(5.dp)) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text,  style = MaterialTheme.typography.titleSmall , fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SynchronizationReportScreenPreview() {
    LogifitApppTheme {
        SynchronizationReportScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SynchronizationReportScreenDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        SynchronizationReportScreen()
    }
}