package com.example.logifitappp.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R

@Composable
fun HeaderHome(
    title: String,
    nameUser: String,
    plan: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_pic),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Column(modifier = modifier.padding(start = 16.dp)) {
            Text(
                text = title
//                style = MaterialTheme.typography.caption
            )
            Text(
                text = nameUser,
//                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Blue,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = plan,
                    color = Color.Blue,
//                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(start = 4.dp)
                )
            }
        }
        Spacer(modifier = modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notificaciones",
//            tint = MaterialTheme.colors.primary
        )
    }
}
