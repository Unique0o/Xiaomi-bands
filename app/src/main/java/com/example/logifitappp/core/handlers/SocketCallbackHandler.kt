package com.example.logifitappp.core.handlers

interface SocketCallbackHandler {
    fun onConnectionEstablished()
    fun onSocketRead(payload: ByteArray)
}