package com.example.logifitappp.ui.screens.drowsiness_test_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.data.remote.dto.requests.EvaluationAnswerRequest
import com.example.logifitappp.data.remote.dto.response.FetchQuestionResponse
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.ScrollablePage

@Composable
fun DrowsinessTestQuestions(
    answers: Map<Int, EvaluationAnswerRequest>,
    canSign: Boolean,
    data: FetchQuestionResponse,
    navigation: NavHostController,
    onContinue: () -> Unit,
    onQuestionAnswered: (Int, String) -> Unit,
    onQuestionDetailChanged: (Int, String) -> Unit
) {
    val evaluation = data.evaluation
    val questions = data.items

    ScrollablePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = evaluation.name
            )
        }
    ) {
        item {
            evaluation.description?.let {
                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = it,
                    typography = MaterialTheme.typography.displayLarge
                )

                Spacer(Modifier.height(2.dp))
            }

            evaluation.indications?.let {
                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = it,
                    typography = MaterialTheme.typography.labelLarge
                )

                Spacer(Modifier.height(16.dp))
            }
        }

        items(questions) {
            val answer = answers[it.id]
            val yesSelected = answer?.answer == "Si"
            val noSelected = answer?.answer == "No"

            Text(
                text = it.item,
                typography = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))

            Row {
                Box(
                    Modifier
                        .background(
                            if (yesSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                        )
                        .weight(1f)
                        .padding(vertical = 8.dp)
                        .clickable {
                            onQuestionAnswered(it.id, "Si")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        color = if (yesSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        text = stringResource(R.string.yes),
                        typography = MaterialTheme.typography.titleLarge
                    )
                }

                Box(
                    Modifier
                        .background(
                            if (noSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                        )
                        .weight(1f)
                        .padding(vertical = 8.dp).clickable {
                            onQuestionAnswered(it.id, "No")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        color = if (noSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        text = stringResource(R.string.no),
                        typography = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                leadingIcon = Icons.Default.Edit,
                onValueChange = { detail ->
                    onQuestionDetailChanged(it.id, detail.text)
                },
                placeholder = stringResource(id = R.string.placeholder_detail),
                value = TextFieldValue(answer?.detail ?: ""),
            )

            Spacer(Modifier.height(16.dp))
        }

        item {
            Button(
                enabled = canSign,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.button_continue),
                onClick = onContinue
            )
        }
    }
}