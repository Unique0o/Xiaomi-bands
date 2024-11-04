package com.example.logifitappp.core.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object SharingUtils {
    fun shareByBitmap(context: Context, bitmap: Bitmap, filename: String, @StringRes sharingMessage: Int) {
        val file = File(context.cacheDir, filename).apply {
            parentFile?.mkdirs()
        }

        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        context.startActivity(
            Intent.createChooser(shareIntent, context.getString(sharingMessage)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}