package com.example.logifitappp.ui.components.tests

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionTest(
    questionText: String,
    onOptionSelected: (Boolean) -> Unit,
    textFieldValue: String,
    onTextFieldChange: (String) -> Unit
) {
    var selectedOption by remember { mutableStateOf<Boolean?>(null) }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()) {
        Text(
            text = questionText,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        ) {
            RadioButton(
                selected = selectedOption == true,
                onClick = {
                    selectedOption = true
                    onOptionSelected(true)
                },
                modifier = Modifier.padding(start = 22.dp)
            )
            Text(
                text = stringResource(id = R.string.yes),
            )

            Spacer(modifier = Modifier.weight(1f))

            RadioButton(
                selected = selectedOption == false,
                onClick = {
                    selectedOption = false
                    onOptionSelected(false)
                },
            )
            Text(
                text = "No",

            )
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.detail) ,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            TextField(
                value = textFieldValue,
                onValueChange = onTextFieldChange,
                colors =  TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.outline,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionTestPreview() {
    var textFieldValue by remember { mutableStateOf("") }
    LogifitApppTheme {
        QuestionTest(
            questionText = "¿Está usted de acuerdo?",
            onOptionSelected = { isSelected ->

            },
            textFieldValue = textFieldValue,
            onTextFieldChange = { newValue ->
                textFieldValue = newValue
            }
        )
    }
}