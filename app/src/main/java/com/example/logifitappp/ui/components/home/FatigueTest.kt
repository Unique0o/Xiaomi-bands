package com.example.logifitappp.ui.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.theme.Stone470

@Composable
fun FatigueTestItem(
    date : String
) {
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
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = stringResource(id = R.string.title_test_fatiga).uppercase(),
                    color = Blue690,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 1.dp)
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
                text = stringResource(id = R.string.result_date) + " "+ date,
                style = MaterialTheme.typography.labelSmall,
                color = Stone470,
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
            )
            ShareButton(
                title = stringResource(id = R.string.share),
                iconSize = 10.dp,
                onClick = { /*TODO*/ }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FatigueTestItemPreview() {
    LogifitApppTheme {
        FatigueTestItem( date = "24/10/2023")
    }
}

@Preview
@Composable
fun FatigueTestItemDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        FatigueTestItem(date = "24/10/2023")
    }
}