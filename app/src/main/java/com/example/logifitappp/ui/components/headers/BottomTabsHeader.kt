package com.example.logifitappp.ui.components.headers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.theme.Orange510

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomTabsHeader(
    children: @Composable () -> Unit = {},
    navigation: NavHostController
) {
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        TopAppBar(
            actions = {
                IconButton(
                    onClick = { navigation.navigate(MainRoutes.Notifications) }
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            navigationIcon = {
                Image(
                    painter = painterResource(id = R.drawable.user1),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
            },
            title = {
                Column {
                    Text(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = stringResource(id = R.string.greeting),
                        typography = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        text = "MARIA MERCEDES",
                        typography = MaterialTheme.typography.displayLarge
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Orange510,
                            modifier = Modifier.size(12.dp)
                        )

                        Text(
                            text = stringResource(id = R.string.lite_plan).uppercase(),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            typography = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        )

        children()
    }
}