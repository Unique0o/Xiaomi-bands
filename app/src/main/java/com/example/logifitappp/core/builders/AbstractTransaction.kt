package com.example.logifitappp.core.builders

import com.example.logifitappp.core.utils.DateTimeUtils
import java.util.Date
import java.util.Locale

abstract class AbstractTransaction(private val taskName: String) {
    private val createdAt = System.currentTimeMillis()

    protected fun getCreationTime(): String = DateTimeUtils.formatDateTime(Date(createdAt))

    fun getTaskName(): String {
        return taskName
    }

    override fun toString(): String {
        return String.format(Locale.US, "%s: Transaction task: %s with %d actions", getCreationTime(), getTaskName(), getActionCount())
    }

    abstract fun getActionCount(): Int
}