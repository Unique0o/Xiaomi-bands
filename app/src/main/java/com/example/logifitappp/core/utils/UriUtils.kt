package com.example.logifitappp.core.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.IOException

fun Uri.toFile(context: Context): File {
    val inputStream = context.contentResolver.openInputStream(this)
    val tempFile = context.createImageFile()

    return try {
        tempFile.outputStream().use { fileOut -> inputStream?.copyTo(fileOut) }
        inputStream?.close()
        tempFile
    } catch (e: Exception) {
        throw IOException("can't generate file")
    }
}