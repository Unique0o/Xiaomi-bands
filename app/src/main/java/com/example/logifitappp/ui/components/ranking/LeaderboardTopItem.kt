package com.example.logifitappp.ui.components.ranking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.logifitappp.R
import com.example.logifitappp.data.User
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Stone470

@Composable
fun LeaderboardTopItem(
    imageUri: Int?,
    isOwner: Boolean,
    placeImageResource: Int,
    summary: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isOwner) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Owner",
                tint = Blue690,
                modifier = Modifier.size(24.dp)
            )
        }

        Image(
            painter = painterResource(id = placeImageResource),
            contentDescription = "Place",
            modifier = Modifier.size(60.dp)
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .border(4.dp, Blue690, CircleShape)
                .padding(2.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = imageUri ?: R.drawable.user1),
                contentDescription = "Profile Photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier.width(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Stone470,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
            Text(
                text = summary,
                color = Blue690,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TopRankingSection(topUsers: List<User>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LeaderboardTopItem(
            imageUri = topUsers[1].imageResId,
            isOwner = topUsers[1].isOwner,
            placeImageResource = R.drawable.silver_medal,
            summary = topUsers[1].score.toString(),
            title = topUsers[1].name,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .offset(x = 20.dp)
                .zIndex(1f)
        )
        LeaderboardTopItem(
            imageUri = topUsers[0].imageResId,
            isOwner = topUsers[0].isOwner,
            placeImageResource = R.drawable.gold_medal,
            summary = topUsers[0].score.toString(),
            title = topUsers[0].name,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .offset(y = (-20).dp)
                .zIndex(2f)
        )
        LeaderboardTopItem(
            imageUri = topUsers[2].imageResId,
            isOwner = topUsers[2].isOwner,
            placeImageResource = R.drawable.third_place,
            summary = topUsers[2].score.toString(),
            title = topUsers[2].name,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .offset(x = (-20).dp)
                .zIndex(1f)
        )
    }
}

@Composable
fun ClassificationScreen(topUsers: List<User>) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopRankingSection(topUsers)
    }
}

@Preview
@Composable
fun ClassificationScreenPreview() {
    ClassificationScreen(
        topUsers = listOf(
            User("User 1", 100, R.drawable.user1, false, 1),
            User("User 2", 200, R.drawable.user1, false, 2),
            User("User 3", 300, R.drawable.user1, false, 3)
        )
    )
}