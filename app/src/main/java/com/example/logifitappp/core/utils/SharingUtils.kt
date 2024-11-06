package com.example.logifitappp.core.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

object SharingUtils {
    fun share(context: Context, bitmap: Bitmap, filename: String, @StringRes sharingMessage: Int) {
        val file = File(context.cacheDir, filename).apply {
            parentFile?.mkdirs()
        }

        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

        share(
            context,
            file,
            "image/png",
            sharingMessage
        )
    }

    fun share(context: Context, file: File, mime: String, @StringRes sharingMessage: Int) {
        share(
            context,
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file),
            mime,
            sharingMessage
        )
    }

    fun share(context: Context, body: ResponseBody, filename: String, @StringRes sharingMessage: Int) {
        val file = File(context.cacheDir, filename).apply {
            parentFile?.mkdirs()
        }

        val inputStream = body.byteStream()

        FileOutputStream(file).let { outputStream ->
            val buffer = ByteArray(4096)
            var read: Int

            while (inputStream.read(buffer).also { read = it } != -1) outputStream.write(buffer, 0, read)

            outputStream.flush()
        }

        share(
            context,
            file,
            "application/pdf",
            sharingMessage
        )
    }

    private fun share(context: Context, uri: Uri, mime: String, @StringRes sharingMessage: Int) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = mime
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