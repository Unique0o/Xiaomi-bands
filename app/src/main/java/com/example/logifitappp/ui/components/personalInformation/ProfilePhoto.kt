package com.example.logifitappp.ui.components.personalInformation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.ProgressiveImage
import kotlinx.coroutines.launch

@Composable
fun ProfilePhoto(profilePic: String, onPhotoClick: () -> Unit) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.profile_photo),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.user1),
//                    contentDescription = "Profile Photo",
//                    modifier = Modifier
//                        .size(100.dp)
//                        .clip(CircleShape)
//                        .align(Alignment.CenterEnd),
//                    contentScale = ContentScale.Crop
//                )

                ProgressiveImage(
                    default = R.drawable.ic_default_profile_photo,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterEnd)
                        .clickable {
                        },
                    url = profilePic
                )
                IconButton(
                    onClick = onPhotoClick,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-6).dp, y = (-6).dp)
                        .background(Color.Black, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = "Change photo",
                        tint = Color.White
                    )
                }
            }

        }
        HorizontalDivider(color = Color.LightGray)
    }
}