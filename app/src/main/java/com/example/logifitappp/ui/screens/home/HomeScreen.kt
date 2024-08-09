import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.CardItem
import com.example.logifitappp.ui.components.home.CardItemWithDescription
import com.example.logifitappp.ui.components.home.HeaderHome
import com.example.logifitappp.ui.screens.home.SmartBandScreen
import com.example.logifitappp.ui.screens.home.TestFatigaItem
import com.example.logifitappp.ui.screens.home.TestsSection
import com.example.logifitappp.ui.theme.LogifitApppTheme

val LightBlue = Color(0xFFE8F1FF)
val DarkBlue = Color(0xFF2E5B9A)
val Green = Color(0xFF4CAF50)

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        BackgroundCurve(modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HeaderHome(
                title = "Bienvenido de vuelta",
                nameUser = "MARIA MERCEDEZ",
                plan = stringResource(id = R.string.plan)
            )
            OptionsList()
//            OptionCard()
            SmartBandScreen()
            TestsSection()
            TestFatigaItem()
        }
    }

}

@Composable
fun BackgroundCurve(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val cornerRadius = 40f
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height * 0.3f - cornerRadius)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    size.width - cornerRadius * 2,
                    size.height * 0.3f - cornerRadius * 2,
                    size.width,
                    size.height * 0.3f
                ), startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false
            )
            quadraticBezierTo(
                size.width / 2f, size.height * 0.3f + 40f, cornerRadius, size.height * 0.3f
            )
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    0f, size.height * 0.3f - cornerRadius * 2, cornerRadius * 2, size.height * 0.3f
                ), startAngleDegrees = 90f, sweepAngleDegrees = 90f, forceMoveTo = false
            )
            close()
        }
        drawPath(path, Color.White)
    }
}

@Composable
fun OptionsList() {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        item {
            CardItem(
                title = stringResource(id = R.string.schedule),
                status = stringResource(id = R.string.status),
                R.drawable.ic_clock,
                Green,
                modifier = Modifier.padding()
            )
        }
        item {
            CardItem(
                title = stringResource(id = R.string.my_location),
                status = stringResource(id = R.string.PGT),
                R.drawable.ic_location,
                Green,
                modifier = Modifier.padding()
            )
        }

    }
}

@Composable()
fun OptionCard(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            CardItemWithDescription(
                title = stringResource(id = R.string.my_device),
                description = stringResource(id = R.string.card_description),
                iconRes = R.drawable.ic_watch,
                modifier = modifier.padding(),
                onClick = { /*TODO*/ })
        }
        item {
            CardItemWithDescription(
                title = stringResource(id = R.string.title_my_test),
                description = stringResource(id = R.string.card_description_message),
                iconRes = R.drawable.ic_menu_test,
                modifier = modifier.padding(),
                onClick = { /*TODO*/ })
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LogifitApppTheme {
        MainScreen()

    }
}