package com.example.logifitappp.ui.screens.statistics


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.statistics.CardStatistics
import com.example.logifitappp.ui.components.statistics.RangeDateSelect
import com.example.logifitappp.ui.theme.LogifitApppTheme


@Composable
fun ReportScreenUI( navigation: NavHostController) {
    SimplePage(
        content = {
            CardStatistics()
            IconText(
                Icons.Default.CheckCircle,
                text = stringResource(id = R.string.updated_information) + " "+ "24/03/2023, 8.25AM",
            )
            RangeDateSelect(
                label = "27/07/2023",
                onLeftPress = { /*  */ },
                onRightPress = { /*  */ }
            )

        }
    )
}




@Composable
fun IconText(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier =  Modifier.padding(16.dp)) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text,  style = MaterialTheme.typography.titleSmall)
    }
}

@Preview(showBackground = true)
@Composable
fun ReportScreenPreview() {
    LogifitApppTheme {
        ReportScreenUI(rememberNavController())
    }

}