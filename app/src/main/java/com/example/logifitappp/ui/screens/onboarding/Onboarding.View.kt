package com.example.logifitappp.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Link
import com.example.logifitappp.ui.components.MarkdownText
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.theme.Sky320
import com.example.logifitappp.viewmodel.AppViewModel

@Composable
fun OnboardingView(
   appViewModel: AppViewModel
) {
    val pagerState = rememberPagerState(pageCount = {
        3
    })

    Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { innerPadding ->
        Box(Modifier.fillMaxSize()) {
            Image(
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(if (isSystemInDarkTheme()) R.drawable.ic_onboarding_background_dark else R.drawable.ic_onboarding_background)
            )

            Column(
                Modifier
                    .padding(
                        bottom = innerPadding.calculateBottomPadding() + 16.dp,
                        end = 16.dp,
                        start = 16.dp,
                        top = innerPadding.calculateTopPadding()
                    )
                    .fillMaxSize()
            ) {
                Spacer(Modifier.height(32.dp))

                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Link(
                        text = stringResource(R.string.omit),
                        textColor = Color.White
                    ) { appViewModel.omitOnboarding() }
                }

                Spacer(Modifier.height(24.dp))

                HorizontalPager(
                    modifier = Modifier.weight(1f),
                    reverseLayout = false,
                    state = pagerState
                ) { page ->
                    when (page) {
                        0 -> OnboardingItem(
                            image = R.drawable.ic_operator_onboarding_1,
                            title = stringArrayResource(R.array.operator_onboarding_title)[page]
                        ) {
                            MarkdownText(
                                boldTextTypography = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
                                normalTextTypography = MaterialTheme.typography.labelLarge.copy(color = Color.White),
                                text = stringArrayResource(R.array.operator_onboarding_subtitle)[page],
                                textAlign = TextAlign.Center
                            )
                        }

                        1 -> OnboardingItem(
                            image = R.drawable.ic_operator_onboarding_2,
                            title = stringArrayResource(R.array.operator_onboarding_title)[page]
                        ) {
                            MarkdownText(
                                boldTextTypography = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
                                normalTextTypography = MaterialTheme.typography.labelLarge.copy(color = Color.White),
                                text = stringArrayResource(R.array.operator_onboarding_subtitle)[page],
                                textAlign = TextAlign.Center
                            )
                        }

                        2 -> OnboardingItem(
                            image = R.drawable.ic_onboarding_3,
                            title = stringArrayResource(R.array.operator_onboarding_title)[page]
                        ) {
                            Button(
                                backgroundColor = Color.White,
                                colorText = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { appViewModel.omitOnboarding() },
                                text = stringResource(R.string.button_active_bluetooth)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    Modifier.wrapContentHeight().fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val bgColor = if (iteration == pagerState.currentPage) Color.White else Sky320

                        Box(
                            Modifier
                                .padding(6.dp)
                                .size(4.dp)
                                .background(bgColor, CircleShape)
                        )
                    }
                }
            }
        }
    }
}