package com.example.logifitappp.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Green298

@Composable
fun CardItemButton(
    title: String,
    description: String,
    icon: Int,
    iconButton: Int,
    buttonColor: Color,
    onClick: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .background(buttonColor, CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = iconButton),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}



@Preview
@Composable
fun CardItemPreview() {
    CardItemButton(
        title = stringResource(id = R.string.my_device),
        description = stringResource(id = R.string.card_description),
        icon = R.drawable.ic_watch,
        iconButton = R.drawable.ic_add,
        buttonColor = Green298,
        onClick = { /*TODO*/ }
    )

}
