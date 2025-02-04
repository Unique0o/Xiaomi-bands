import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.data.User
import com.example.logifitappp.ui.components.ranking.CardTemplate
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Stone470

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
fun UserListItemCard(user: User) {
    CardTemplate(
        backgroundColor = if (user.id % 2 == 0) MaterialTheme.colorScheme.surfaceContainer else Blue690.copy(alpha = 0.1f)
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
                painter = painterResource(id = user.imageResId),
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
