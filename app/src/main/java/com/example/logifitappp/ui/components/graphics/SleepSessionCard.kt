import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.ConnectedIndicator


@Composable
fun SleepSessionCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SessionHeader()
            ProgressBar()
            SleepTypeDetails()
        }
    }
}

@Composable
fun SessionHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bed),
                contentDescription = stringResource(id = R.string.content_description_sleep),
                tint = Color(0xFF4285F4)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "14:31 - 15:26",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4285F4)
            )
        }
        ConnectedIndicator(
            text = "55min",
            color = Color(0xFF4CAF50),
            backgroundColor = Color(0xFFE8F5E9),
            pointColor = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun ProgressBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(Color(0xFF8AB4F8), RoundedCornerShape(4.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.76f)
                .height(8.dp)
                .background(Color(0xFF4285F4), RoundedCornerShape(4.dp))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.11f)
                .height(8.dp)
                .background(Color(0xFF7E57C2), RoundedCornerShape(4.dp))
        )
    }
}

@Composable
fun SleepTypeDetails() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SleepTypeInfo(
                color = Color(0xFF8AB4F8),
                type = stringResource(id = R.string.light_sleep),
                percentage = "76%",
                duration = "23min"
            )
            SleepTypeInfo(
                color = Color(0xFF4285F4),
                type = stringResource(id = R.string.deep_sleep),
                percentage = "13%",
                duration = "13min"
            )
        }
        SleepTypeInfo(
            color = Color(0xFF7E57C2),
            type = stringResource(id = R.string.rem_sleep),
            percentage = "11%",
            duration = "9min"
        )
    }
}

@Composable
fun SleepTypeInfo(
    color: Color,
    type: String,
    percentage: String,
    duration: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = "$type ($percentage)",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = duration,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}