package com.example.logifitappp.ui.screens.all_in_one

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.forms.OutlinedTextField
import com.example.logifitappp.viewmodel.views.AllInOneViewModel

@Composable
fun AllInOneHeader(
    allInOneViewModel: AllInOneViewModel,
    navigation: NavHostController
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
            onClick = { navigation.navigate(MainRoutes.WearableDetection) }
        ) {
            Icon(
                contentDescription = null,
                imageVector = Icons.Default.Add,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }

        Spacer(Modifier.width(16.dp))

        OutlinedTextField(
            leadingIcon = Icons.Default.Search,
            onValueChange = { allInOneViewModel.search(it) },
            placeholder = stringResource(id = R.string.placeholder_search),
            value = allInOneViewModel.state.searchText
        )
    }
}