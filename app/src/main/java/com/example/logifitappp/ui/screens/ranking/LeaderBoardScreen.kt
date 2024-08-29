import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.data.repository.LeaderboardRepositoryImpl
import com.example.logifitappp.ui.components.headers.CustomTopBar
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.ranking.TopRankingSection
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.LeaderBoard.LeaderboardViewModel
import com.example.logifitappp.viewmodel.views.LeaderBoard.LeaderboardViewModelFactory


@Composable
fun LeaderboardScreen(
    navigation: NavHostController
) {
    val repository = LeaderboardRepositoryImpl()
    val factory = LeaderboardViewModelFactory(repository)
    val leaderboardViewModel: LeaderboardViewModel = viewModel(factory = factory)
    val uiState by leaderboardViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        leaderboardViewModel.fetchLeaderboard()
    }
    SimplePage(
        topBar = {
            CustomTopBar(
                title = stringResource(id = R.string.title_leaderboard_topbar),
            )

        },
        content = {
            DateDisplay()
            Spacer(modifier = Modifier.height(20.dp))

            when {
                uiState.isLoading -> {
                }

                uiState.error != null -> {
                }

                else -> {
                    TopRankingSection(uiState.topThreeUsers)
                    UserList(uiState.nextThreeUsers)
                }
            }

        }
    )

}

@Preview
@Composable
fun LeaderboardScreenPreview() {

    LogifitApppTheme {
        LeaderboardScreen(
            rememberNavController()
        )
    }
}
@Preview
@Composable
fun LeaderboardScreenDarkPreview() {

    LogifitApppTheme(darkTheme = true) {
        LeaderboardScreen(
            rememberNavController()
        )
    }
}