package com.example.logifitappp.ui.screens.meditation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.core.utils.avoidBottom
import com.example.logifitappp.core.utils.plus
import com.example.logifitappp.enums.AudioFileEnum
import com.example.logifitappp.ui.components.headers.BottomTabsHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.MeditationViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MeditationView(
    appViewModel: AppViewModel,
    drawerState: DrawerState,
    navigation: NavHostController,
    contentPadding: PaddingValues? = null
) {
    val meditationViewModel = hiltViewModel<MeditationViewModel, MeditationViewModel.MeditationViewModelFactory> {
        it.create(appViewModel.user)
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
           FlowRow(Modifier.fillMaxWidth(), maxItemsInEachRow = 2) {
               AudioFileEnum.entries.mapIndexed { index, it ->
                   Card(
                       modifier = Modifier
                           .height(180.dp)
                           .weight(1f)
                           .padding(start = if (index % 2 == 0) 0.dp else 6.dp, end = if (index % 2 == 0) 6.dp else 0.dp)
                           .padding(bottom = 12.dp)
                           .clickable { meditationViewModel.play(it) },
                       shape = RoundedCornerShape(8.dp),
                   ) {
                       MeditationItem(
                           isPlaying = meditationViewModel.isPlaying,
                           isProgressVisible = meditationViewModel.currentAudio == it,
                           item = it,
                           progress = meditationViewModel.progress
                       )
                   }
               }
           }
       }
    }
}