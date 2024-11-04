package com.example.logifitappp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.data.models.UserModel

@Composable
fun SideBarContent(
    navigation: NavHostController,
    user: UserModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(horizontal = 8.dp, vertical = 40.dp)
        ) {
            ProgressiveImage(
                default = R.drawable.ic_default_profile_photo,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                url = user.profilePhoto
            )

            Text(
                text = user.getFullname()
            )

            Text(
                text = user.getRole()
            )
        }
    }
}