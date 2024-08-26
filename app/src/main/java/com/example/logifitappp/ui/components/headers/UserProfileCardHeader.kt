package com.example.logifitappp.ui.components.headers


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R

import com.example.logifitappp.ui.theme.Orange390

@Composable
fun UserProfileCard(
    userName: String,
    userType: String,
    plan: String,
    modifier: Modifier = Modifier,
    @DrawableRes profileImageRes: Int,
    onNotificationClick: () -> Unit,
    bodyComponent: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = profileImageRes),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Bienvenido de vuelta",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = userName.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Orange390,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = plan,
                            color = Orange390,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = modifier.padding(start = 4.dp)
                        )
                    }
                }

                IconButton(onClick = onNotificationClick) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            }
            bodyComponent()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileCardPreview() {
    UserProfileCard(
        userName = "MARIA MERCEDES",
        userType = "PREMIUM",
        profileImageRes = R.drawable.user1,
        onNotificationClick = { },
        plan = "PREMIUM",
        bodyComponent = {}
    )
}