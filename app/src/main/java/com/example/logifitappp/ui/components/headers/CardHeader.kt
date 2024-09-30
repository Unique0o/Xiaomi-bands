package com.example.logifitappp.ui.components.headers


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.utils.getLicenseInfo

@Composable
fun CardHeader(
    user: UserModel,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit,
    onProfileImageClick: () -> Unit,
    bodyComponent: @Composable () -> Unit,
) {
    val licenseInfo = getLicenseInfo(user.license)

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user1),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onProfileImageClick)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.welcome),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${user.firstName} ${user.lastName}".uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id =licenseInfo.icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = licenseInfo.label,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
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
fun CardHeaderPreview() {
    CardHeader(
        userName = "MARIA MERCEDES",
        userType = "PREMIUM",
        profileImageRes = R.drawable.user1,
        onNotificationClick = { },
        onProfileImageClick = { },
        plan = "PREMIUM",
        bodyComponent = {}
    )
}
