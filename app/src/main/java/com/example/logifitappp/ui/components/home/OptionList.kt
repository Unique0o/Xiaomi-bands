package com.example.logifitappp.ui.components.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70

@Composable
fun OptionsList() {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        item {
            CardItem(
                title = stringResource(id = R.string.schedule),
                status = stringResource(id = R.string.status),
                R.drawable.ic_clock,
                statusColor = Green298,
                backgroundColor = Lime70,
                modifier = Modifier.padding()
            )
        }
        item {
            CardItem(
                title = stringResource(id = R.string.my_location),
                status = stringResource(id = R.string.PGT),
                R.drawable.ic_location,
                statusColor = Green298,
                backgroundColor = Lime70,
                modifier = Modifier.padding()
            )
        }

    }
}