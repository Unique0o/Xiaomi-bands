package com.example.logifitappp.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.LogifitApppTheme
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.ui.components.graphics.CardLayout
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.Stone470
import com.example.logifitappp.ui.theme.White

@Composable
fun CardTest(modifier: Modifier = Modifier) {
    CardLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(310.dp),
        style = modifier.background(MaterialTheme.colorScheme.surface),
        label = stringResource(id = R.string.battery_status),
        labelStyle = MaterialTheme.typography.labelMedium.copy(Blue690),
        icon = painterResource(id = R.drawable.ic_battery),
        bodyComponent = {

            Spacer(modifier = modifier.height(8.dp))

            WarningMessage(
                title = stringResource(id = R.string.warning_title),
                color = Stone470,
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusCard(stringResource(id = R.string.content_description_sleep).uppercase(), true)
                StatusCard(stringResource(id = R.string.face_status), false)
            }

        },
        suffixComponent = {
            ConnectedIndicator(
                text = stringResource(id = R.string.connected),
                color = Green298,
                backgroundColor = Lime70,
                pointColor = Green298
            )
        }
    )
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LogifitApppTheme {
        CardTest()
    }
}

@Preview
@Composable
fun GreetingPreviewDark() {
    LogifitApppTheme(darkTheme = true) {
       CardTest()
    }
}