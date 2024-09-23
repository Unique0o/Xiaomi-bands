package com.example.logifitappp.ui.screens.tests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.headers.BackHeader
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.tests.SelectableQuestion
import com.example.logifitappp.viewmodel.views.tests.FatigueSelfAssessmentViewModel

@Composable
fun FatigueSelfAssessmentTestScreen(navigation: NavHostController) {
    val testViewModel: FatigueSelfAssessmentViewModel = viewModel()
    val questions by testViewModel.questions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        BackHeader(
            navigation = navigation,
            title = stringResource(id = R.string.title_test_fatiga)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.test_description),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                Text(
                    text = stringResource(id = R.string.description_fatigue_test),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.inverseSurface
                )
            }

            items(questions.size) { index ->
                val question = questions[index]
                val selectedOption by remember { mutableStateOf("") }
                var detailText by remember { mutableStateOf(TextFieldValue("")) }

                SelectableQuestion(
                    questionText = stringResource(id = testViewModel.getTranslatedQuestionResourceId(question)),
                    options = listOf(stringResource(id = R.string.yes), "No"),
                    selectedOption = selectedOption,
                    onOptionSelected = { testViewModel.updateAnswer(question.id, it) },
                    detailText = detailText,
                    onDetailTextChanged = { detailText = it }
                )
                if (index != questions.lastIndex) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { /*TODO*/ },
                    text = stringResource(id = R.string.finish),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FatigueSelfAssessmentTestScreenPreview() {
    LogifitApppTheme {
        FatigueSelfAssessmentTestScreen(navigation = rememberNavController())
    }
}
@Preview(showBackground = true)
@Composable
fun FatigueSelfAssessmentTestScreenDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        FatigueSelfAssessmentTestScreen(navigation = rememberNavController())
    }
}