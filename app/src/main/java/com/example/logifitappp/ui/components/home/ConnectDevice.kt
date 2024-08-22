package com.example.logifitappp.ui.components.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R

@Composable()
fun ConnectDevice(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            CardItemWithDescription(
                title = stringResource(id = R.string.my_device),
                description = stringResource(id = R.string.card_description),
                iconRes = R.drawable.ic_watch,
                modifier = modifier.padding(),
                onClick = { /*TODO*/ })
        }
        item {
            CardItemWithDescription(
                title = stringResource(id = R.string.title_my_test),
                description = stringResource(id = R.string.card_description_message),
                iconRes = R.drawable.ic_menu_test,
                modifier = modifier.padding(),
                onClick = { /*TODO*/ })
        }

    }
}