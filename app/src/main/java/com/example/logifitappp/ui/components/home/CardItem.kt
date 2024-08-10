package com.example.logifitappp.ui.components.home

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R

val DarkBlue = Color(0xFF2E5B9A)

@Composable
fun CardItem(
    title: String,
    status: String, iconRes: Int,
    statusColor: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(56.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = DarkBlue
            )
            Text(
                text = title,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                color = DarkBlue,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.width(16.dp))

            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x1A4CAF50))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = modifier
                            .size(8.dp)
                            .background(Color(0xFF4CAF50), CircleShape)
                    )

                    Spacer(modifier = modifier.width(4.dp))

                    Text(
                        text = status,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}

@Composable
fun CardItemWithDescription(
    title: String,
    description: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
//        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
//        backgroundColor = Color.White
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = DarkBlue
                )
                Text(
                    text = title,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    color = DarkBlue,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onClick,
                    modifier = modifier
                        .background(DarkBlue, CircleShape)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_content_description),
                        tint = Color.White
                    )
                }
            }
            Text(
                text = description,
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = modifier.padding(top = 8.dp, start = 40.dp)
            )
        }
    }

}
