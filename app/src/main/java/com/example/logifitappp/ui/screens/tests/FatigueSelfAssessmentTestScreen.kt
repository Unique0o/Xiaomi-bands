package com.example.logifitappp.ui.screens.tests

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.headers.BackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.components.tests.QuestionTest

@Composable
fun FatigueSelfAssessmentTestScreen( navigation: NavHostController) {
    var textFieldValue by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BackHeader(
            navigation = navigation,
            title = stringResource(id = R.string.title_test_fatiga)
        )
        SimplePage(

            content = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.test_description))
                }
                Text(text = stringResource(id = R.string.description_fatigue_test))
                QuestionTest(
                    questionText = "¿Está usted de acuerdo?",
                    onOptionSelected = { isSelected -> },
                    textFieldValue = textFieldValue,
                    onTextFieldChange = { newValue -> textFieldValue = newValue }
                )
            }
        )
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