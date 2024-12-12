package com.android.internal.telephony

interface ITelephony: android.os.IInterface {
    fun endCall(): Boolean
    fun answerRingingCall()
    fun silenceRinger()
}