package com.example.logifitappp.core.broadcasters

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class PhoneCallBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val LAST_STATE = TelephonyManager.CALL_STATE_IDLE
    }
}