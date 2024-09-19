package com.example.logifitappp.exceptions

import com.example.logifitappp.enums.AppStatusCodeEnum
import java.io.IOException

class HttpConsumerException(private val status: AppStatusCodeEnum): IOException(status.name) {
    fun getStatus() = status

    override fun toString() = "HttpConsumerException: ${getStatus()}"
}