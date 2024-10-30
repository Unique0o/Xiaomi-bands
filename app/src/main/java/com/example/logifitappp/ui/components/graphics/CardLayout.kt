package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardElevation
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70

@Composable
fun CardLayout(
    modifier: Modifier = Modifier,
    bodyComponent: @Composable () -> Unit,
    icon: Painter? = null,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    iconSize: Dp = 24.dp,
    label: String,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    labelStyle: TextStyle = TextStyle.Default,
    textcolor: Color = MaterialTheme.colorScheme.primary,
    fontWeight: FontWeight = FontWeight.Normal,
    style: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    suffixComponent: @Composable (() -> Unit)? = null,
    cardBackgroundColor: Color = MaterialTheme.colorScheme.outline,
    titleAlternateText: String? = null,
    subtitleAlternateText: String = "",
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(style).padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = elevation,
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        ),
        onClick = onClick ?: {}
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    icon?.let {
                        Icon(
                            painter = it,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(iconSize)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Column {
                        Text(
                            text = label,
                            style = labelStyle.copy(color = textcolor),
                            fontSize = labelStyle.fontSize,
                            fontWeight = labelStyle.fontWeight
                        )
                        if(titleAlternateText != null){
                            Row {
                                Text(
                                    text = titleAlternateText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = subtitleAlternateText,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                if (suffixComponent != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    suffixComponent()
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            bodyComponent()
        }
    }
}



@Preview
@Composable
fun CardTemplatePreview() {
    CardLayout(
        modifier = Modifier
            .padding(start = 8.dp),
        bodyComponent = {},
        icon = painterResource(id = R.drawable.ic_shoe_sneaker),
        iconSize = 24.dp,
        label = "Steps",
        labelStyle = MaterialTheme.typography.labelMedium,
        style =  Modifier.padding(8.dp),
        suffixComponent = {
            //ConnectedIndicator(text = "10 Steps", color = Green298 , backgroundColor = Lime70, pointColor = Green298)
        }
    )
}