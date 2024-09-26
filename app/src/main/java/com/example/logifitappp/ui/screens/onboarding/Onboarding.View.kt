package com.example.logifitappp.ui.screens.onboarding

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.onboarding.CenteredContainer
import com.example.logifitappp.ui.components.onboarding.OnboardingFragment
import com.example.logifitappp.ui.components.onboarding.PageIndicator
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.onboarding.OnboardingViewModel


@Composable
fun OnboardingView(
   navigation: NavHostController
) {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val isAdmin by viewModel.isAdmin.collectAsState()
    val shouldNavigateToMain by viewModel.shouldNavigateToMain.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 3 })

    val onNavigateToMain: () -> Unit = {
        navigation.navigate(MainRoutes.Home) {
            popUpTo(MainRoutes.Onboarding) { inclusive = true }
        }
    }

    LaunchedEffect(shouldNavigateToMain) {
        if (shouldNavigateToMain) {
            onNavigateToMain()
            viewModel.onNavigationHandled()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 44.dp, bottom = 40.dp)
        ) {
            SkipButton { viewModel.onSkipClicked() }
            OnboardingPager(pagerState, isAdmin, viewModel::activateBluetooth)
            PageIndicator(pagerState.currentPage, 3)
        }
    }
}

@Composable
private fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.onboarding_background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun SkipButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = onClick) {
            Text(stringResource(R.string.omit), color = Color.White)
        }
    }
}

@Composable
private fun OnboardingPager(
    pagerState: PagerState,
    isAdmin: Boolean,
    onActivateBluetooth: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            CenteredContainer {
                OnboardingFragment(
                    page = page,
                    isAdmin = isAdmin,
                    onActivateBluetooth = onActivateBluetooth
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingViewPreview() {
    LogifitApppTheme {
        OnboardingView(
            navigation = rememberNavController()
        )
    }
}