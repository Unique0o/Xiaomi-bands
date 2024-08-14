package com.example.logifitappp.ui.components.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import androidx.compose.ui.unit.sp

@Composable
fun BackHeader(
    onBackClick: () -> Unit,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Color.Blue
                )
            }
            Text(
                text = "Regresar",
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.Blue, fontSize = 17.sp),
                modifier = Modifier
                    .padding(end = 245.dp)
            )
        }

        // Subtitle
        Text(
            text = subtitle,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge.copy(color = Color.Black, fontSize = 25.sp,),
            modifier = Modifier
            .padding(start = 15.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    BackHeader(
        onBackClick = { /* Acción al hacer clic en la flecha */ },
        subtitle = "Mi sueño",
        modifier = Modifier.background(Color.White)
    )
}
