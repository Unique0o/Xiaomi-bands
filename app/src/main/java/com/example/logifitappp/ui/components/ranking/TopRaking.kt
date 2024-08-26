import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.ranking.RankingUser
import com.example.logifitappp.ui.components.ranking.getMedalResource
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Stone470
import com.example.logifitappp.ui.theme.White

data class RankingUser(
    val id: Int,
    val name: String,
    val score: Int,
    val imageRes: Int
)

@Composable
fun ClassificationScreen(otherUsers: List<RankingUser>) {
    Scaffold(
        topBar = { TopBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)

        ) {
            DateDisplay()
            TopRankingSection()
            UserList( otherUsers)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                "Clasificación",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Blue690
            )
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Usuario",
                    tint = Blue690
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun DateDisplay() {
    Text(
        "Noviembre, 2023",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = Stone470
    )
}

@Composable
fun TopRankingSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        MedalRow()
        Spacer(modifier = Modifier.height(3.dp))
        TopUsersRow()
    }
}

@Composable
fun MedalRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {
        MedalIcon(color = White, rank = 2)
        MedalIcon(color = White,  rank = 1)
        MedalIcon(color = White, rank = 3)
    }
}

@Composable
fun MedalIcon(color: Color, rank: Int) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = getMedalResource(rank)),
            contentDescription = "Rank $rank",
            modifier = Modifier
                .size(70.dp)
                .offset(y = (-7).dp)
        )
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

@Composable
fun TopUsersRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TopUserItem("JOSE PEREZ SANCHEZ", "240", 2)
        TopUserItem("MARIA MERCEDES", "945", 1)
        TopUserItem("LUCIA DELGADO GUEVARA", "165", 3)
    }
}

@Composable
fun TopUserItem(name: String, score: String, position: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserAvatar(
            size = if (position == 1) 130.dp else 90.dp,
            borderColor = when (position) {
                1 -> Color(0xFFFFA726)
                2 -> Color(0xFFE57373)
                else -> Color(0xFFFFD54F)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
        Text(
            text = score,
            color = Blue690,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun UserAvatar(size: Dp, borderColor: Color) {
    Box(
        modifier = Modifier
            .size(size)
            .border(4.dp, borderColor, CircleShape)
            .padding(4.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
    ) {

        Image(
            painter = painterResource(id = R.drawable.user1),
            contentDescription = "Avatar",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun UserList(users: List<RankingUser>) {
    LazyColumn {
        itemsIndexed(
            listOf(
                "RICARDO LOPEZ HUAMAN" to "110",
                "CARLOS EDUARDO TORRES ZARATE" to "105"
            )
        ) { index, (name, score) ->
            UserListItem(index + 4, name, score, users[index])
        }
    }
}

@Composable
fun UserListItem(rank: Int, name: String, score: String, user: RankingUser) {
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

@Composable
@Preview(showBackground = true)
fun MyRankingScreenPreview() {
    val otherUsers = listOf(
        RankingUser(4, "RICARDO LOPEZ HUAMAN", 110, R.drawable.user1),
        RankingUser(5, "CARLOS EDUARDO TORRES ZARATE", 105, R.drawable.user1    )
    )
    ClassificationScreen(otherUsers)
}