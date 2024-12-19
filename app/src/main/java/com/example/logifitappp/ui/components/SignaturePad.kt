package com.example.logifitappp.ui.components

import android.graphics.Bitmap
import android.graphics.Paint
import android.util.Base64
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardBackspace
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.Button
import com.example.logifitappp.ui.components.forms.IconButton
import java.io.ByteArrayOutputStream

@Composable
fun SignaturePad(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onSignatureCaptured: (String) -> Unit
) {
    var path by remember { mutableStateOf(Path()) }
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    var isSigned by remember { mutableStateOf(false) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier.onGloballyPositioned { layoutCoordinates -> size = layoutCoordinates.size },
    ) {
        Canvas(
            Modifier
                .fillMaxSize()
                .clipToBounds()
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            path.moveTo(offset.x, offset.y)
                            currentPosition = offset
                            isSigned = true
                        },
                        onDrag = { change, _ ->
                            path.lineTo(change.position.x, change.position.y)
                            currentPosition = change.position
                            isSigned = true
                        }
                    )
                }
        ) {
            if (currentPosition != Offset.Unspecified) {
                drawPath(path, color = Color.Black, style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
        }

        Row(Modifier.fillMaxWidth()) {
            IconButton(
                backgroundColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                horizontalPadding = 0.dp,
                icon = Icons.AutoMirrored.Rounded.KeyboardBackspace,
                onClick = onBack,
                text = stringResource(id = R.string.go_back),
                textColor = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    isSigned = false
                    path = Path()
                },
                text = stringResource(id = R.string.button_erase)
            )
        }

        Button(
            Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            enabled = isSigned,
            onClick = {
                bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)

                bitmap?.let {
                    val canvas = android.graphics.Canvas(it)
                    canvas.drawColor(android.graphics.Color.WHITE)

                    val paint = Paint().apply {
                        color = android.graphics.Color.BLACK
                        style = Paint.Style.STROKE
                        strokeCap = Paint.Cap.ROUND
                        strokeJoin = Paint.Join.ROUND
                    }

                    canvas.drawPath(path.asAndroidPath(), paint)

                    val outputStream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

                    val byteArray = outputStream.toByteArray()
                    onSignatureCaptured(Base64.encodeToString(byteArray, Base64.DEFAULT))
                }
            },
            text = stringResource(id = R.string.button_continue)
        )
    }
}