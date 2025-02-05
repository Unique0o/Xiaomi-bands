package com.example.logifitappp.exceptions

import com.example.logifitappp.enums.AppStatusCodeEnum

class SynchronizationProcessingException(private val status: AppStatusCodeEnum): IllegalStateException(status.name) {
    fun getStatus() = status

    override fun toString() = "SynchronizationProcessingException: ${getStatus()}"
}