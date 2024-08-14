package com.example.logifitappp.ui.components.ranking

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
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
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Blue690

data class RankingUser(
    val id: Int,
    val name: String,
    val score: Int,
    val imageRes: Int
)

@Composable
fun RankingScreen(
    title: String,
    subtitle: String,
    topUsers: List<RankingUser>,
    otherUsers: List<RankingUser>
) {
    Scaffold(
        topBar = { RankingTopAppBar(title) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            RankingHeader(title, subtitle)
            TopRankedUsers(topUsers)
            OtherRankedUsers(otherUsers)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingTopAppBar(title: String) {
    TopAppBar(
        title = { Text(title, color = Blue690,  textAlign = TextAlign.Center) },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "User",
                    tint = Blue690
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
    )
}

@Composable
fun RankingHeader(title: String, subtitle: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = subtitle,
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

}

@Composable
fun TopRankedUsers(users: List<RankingUser>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        TopUserItem(users[1], 2)
        TopUserItem(users[0], 1)
        TopUserItem(users[2], 3)
    }
}

@Composable
fun TopUserItem(user: RankingUser, rank: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            Image(
                painter = painterResource(id = user.imageRes),
                contentDescription = "User ${user.name}",
                modifier = Modifier
                    .size(if (rank == 1) 100.dp else 80.dp)
                    .clip(CircleShape)
                    .border(2.dp,Blue690, CircleShape),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = getMedalResource(rank)),
                contentDescription = "Rank $rank",
                modifier = Modifier
                    .size(30.dp)
                    .offset(y = (-10).dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = user.name,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 14.sp
        )
        Text(
            text = user.score.toString(),
            color = Blue690,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun OtherRankedUsers(users: List<RankingUser>) {
    LazyColumn {
        items(users) { user ->
            OtherUserItem(user)
        }
    }
}

@Composable
fun OtherUserItem(user: RankingUser) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = if (user.id % 2 == 0) Color.White else Blue690.copy(alpha = 0.1f)),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.id.toString(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(24.dp)
            )
            Image(
                painter = painterResource(id = user.imageRes),
                contentDescription = "User ${user.name}",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = user.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = user.score.toString(),
                color = Blue690,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun getMedalResource(rank: Int): Int {
    return when (rank) {
        1 -> R.drawable.gold_medal
        2 -> R.drawable.silver_medal
        3 -> R.drawable.third_place
        else -> R.drawable.gold_medal
    }
}

// Uso del componente
@Composable
fun MyRankingScreen() {
    val topUsers = listOf(
        RankingUser(2, "JOSE PEREZ SANCHEZ", 240, R.drawable.user1),
        RankingUser(1, "MARIA MERCEDES", 945, R.drawable.user1),
        RankingUser(3, "LUCIA DELGADO GUEVARA", 165, R.drawable.user1)
    )
    val otherUsers = listOf(
        RankingUser(4, "RICARDO LOPEZ HUAMAN", 110, R.drawable.user1),
        RankingUser(5, "CARLOS EDUARDO TORRES ZARATE", 105, R.drawable.user1    )
    )

    RankingScreen(
        title = "Clasificación",
        subtitle = "Noviembre, 2023",
        topUsers = topUsers,
        otherUsers = otherUsers
    )
}
@Composable
@Preview(showBackground = true)
fun MyRankingScreenPreview() {
    MyRankingScreen()
}