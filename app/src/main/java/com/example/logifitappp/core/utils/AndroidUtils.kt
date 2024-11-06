package com.example.logifitappp.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.ParcelUuid
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnPreDraw
import java.io.File

object AndroidUtils {
    @Composable
    fun CaptureComposableAsBitmap(
        onBitmapReady: (Bitmap) -> Unit,
        content: @Composable () -> Unit
    ) {
        AndroidView(factory = { context ->
            val composeView = ComposeView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                setContent { content() }
            }

            /*composeView.post {
                /*val width = composeView.width
                val height = composeView.height

                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)

                composeView.draw(canvas)*/
                val bitmap = composeView.drawToBitmap()
                onBitmapReady(bitmap)
            }*/

            composeView.doOnPreDraw {
                composeView.measure(
                    View.MeasureSpec.makeMeasureSpec(composeView.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )

                composeView.layout(0, 0, composeView.measuredWidth, composeView.measuredHeight)

                val bitmap = Bitmap.createBitmap(
                    composeView.measuredWidth,
                    composeView.measuredHeight,
                    Bitmap.Config.ARGB_8888
                )

                composeView.draw(Canvas(bitmap))
                onBitmapReady(bitmap)
            }

            composeView
        })
    }

    fun getAppVersion(context: Context): String? {
        return try {
            val packageIngo = context.packageManager.getPackageInfo(context.packageName, 0)

            packageIngo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun getFileInCache(context: Context, filename: String): File? {
        val file = File(context.cacheDir, filename)

        if (file.exists()) return file

        return null
    }

    fun toParcelUuids(uuids: Array<out Parcelable>?): Array<ParcelUuid>? {
        if (uuids == null) return null

        return Array(uuids.size) { uuids[it] as ParcelUuid }
    }
}