import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.core.utils.avoidBottom
import com.example.logifitappp.core.utils.plus
import com.example.logifitappp.data.repository.LeaderboardRepositoryImpl
import com.example.logifitappp.ui.components.headers.BottomTabsHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.ui.components.ranking.TopRankingSection
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.LeaderboardViewModel
import com.example.logifitappp.viewmodel.views.LeaderBoard.LeaderboardViewModelFactory

@Composable
fun LeaderboardView(
    appViewModel: AppViewModel,
    drawerState: DrawerState,
    navigation: NavHostController,
    contentPadding: PaddingValues? = null
) {
    val repository = LeaderboardRepositoryImpl()
    val factory = LeaderboardViewModelFactory(repository)
    val leaderboardViewModel: LeaderboardViewModel = viewModel(factory = factory)
    val uiState by leaderboardViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        leaderboardViewModel.fetchLeaderboard()
    }

    ScrollablePage(
        contentPadding = PaddingValues(16.dp).avoidBottom() + contentPadding,
        topBar = {
            BottomTabsHeader(
                appViewModel = appViewModel,
                drawerState = drawerState,
                navigation = navigation
            )
        }
    ) {
        item {
            DateDisplay()
            Spacer(modifier = Modifier.height(20.dp))
        }

        when {
            uiState.isLoading -> {
            }

            uiState.error != null -> {
            }

            else -> {
                item {
                    TopRankingSection(uiState.topThreeUsers)
                }

                items(uiState.nextThreeUsers) { user ->
                    UserListItemCard(user)
                }
            }
        }
    }
}