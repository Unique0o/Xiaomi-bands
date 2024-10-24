package com.example.logifitappp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.example.logifitappp.R

@Composable
fun ProgressiveImage(
    modifier: Modifier = Modifier,
    @DrawableRes default: Int = R.drawable.ic_placeholder_image,
    url: String?
) {
    val painter = rememberAsyncImagePainter(
        model = url ?: default,
        placeholder = painterResource(id = R.drawable.ic_placeholder_image)
    )

    Box(modifier = modifier) {
        Image(
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            painter = painter,
        )
    }
}