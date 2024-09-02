package com.example.logifitappp.ui.components.tests

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.OutlinedTextField

@Composable
fun SelectableQuestion(
    questionText: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    detailText: TextFieldValue,
    onDetailTextChanged: (TextFieldValue) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = questionText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.inverseSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { option ->
                RadioButton(
                    selectedOption = selectedOption == option,
                    onOptionSelected = { if (it) onOptionSelected(option) },
                    text = option
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        OutlinedTextField(
            value = detailText,
            onValueChange = onDetailTextChanged,
            placeholder =  stringResource(id = R.string.detail),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

