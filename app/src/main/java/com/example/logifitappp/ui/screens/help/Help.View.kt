package com.example.logifitappp.ui.screens.help

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import com.example.logifitappp.ui.components.forms.Button
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.ui.components.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage

@Composable
fun HelpView(
    navigation: NavHostController
) {
    val selectedOption = remember { mutableStateOf("") }
    var detailText by remember { mutableStateOf(TextFieldValue("")) }

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.need_help),
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                typography = MaterialTheme.typography.labelLarge,
                text = stringResource(id = R.string.help_instructions),
            )

            val options = listOf(
                stringResource(id = R.string.no_sync),
                stringResource(id = R.string.non_binding),
                stringResource(id = R.string.no_send_sleep),
                stringResource(id = R.string.others)
            )

            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption.value = option
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                }
            }

            if (selectedOption.value == stringResource(id = R.string.others)) {
                OutlinedTextField(
                    value = detailText,
                    onValueChange = { detailText = it },
                    placeholder = stringResource(id = R.string.specify),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Button(
                onClick = {/* */ },
                modifier = Modifier.fillMaxWidth(),
                text= stringResource(id = R.string.ask_help)
            )
        }
    }
}
