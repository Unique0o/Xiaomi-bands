package com.example.logifitappp.core.broadcasters

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.android.internal.telephony.ITelephony
import com.example.logifitappp.core.App
import com.example.logifitappp.core.events.WearableCallControlEvent

class CallControlBroadcastReceiver: BroadcastReceiver() {
    @RequiresApi(api = Build.VERSION_CODES.P)
    @RequiresPermission(Manifest.permission.ANSWER_PHONE_CALLS)
    fun handleCallCmdTelecomManager(cmd: WearableCallControlEvent.Event) {
        try {
            val telecom = App.context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

            when (cmd) {
                WearableCallControlEvent.Event.END,
                WearableCallControlEvent.Event.REJECT -> telecom.endCall()

                WearableCallControlEvent.Event.START,
                WearableCallControlEvent.Event.ACCEPT -> telecom.acceptRingingCall()

                else -> {}
            }
        } catch (e: SecurityException) {
            println("no permission to start or hangup call")
        } catch (e: Exception) {
            println("could not start or hangup call")
        }
    }

    @RequiresPermission(Manifest.permission.ANSWER_PHONE_CALLS)
    override fun onReceive(context: Context, intent: Intent) {
        val cmd = WearableCallControlEvent.Event.entries[intent.getIntExtra("event", 0)]
        
        if (App.isRunningPieOrLater()) handleCallCmdTelecomManager(cmd)
        else {
            when (cmd) {
                WearableCallControlEvent.Event.END,
                WearableCallControlEvent.Event.REJECT,
                WearableCallControlEvent.Event.START -> {
                    try {
                        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                        val clazz = Class.forName(telephonyManager::class.java.name)
                        
                        val method = clazz.getDeclaredMethod("getITelephony")
                        method.isAccessible = true
                        
                        val telephonyService = method.invoke(telephonyManager) as ITelephony

                        when (cmd) {
                            WearableCallControlEvent.Event.END,
                            WearableCallControlEvent.Event.REJECT -> telephonyService.endCall()

                            else -> telephonyService.answerRingingCall()
                        }
                    } catch (e: Exception) {
                        println("could not start or hangup call")
                    }
                }
                
                 else -> {}
            }
        }
    }
    
    companion object {
        const val ACTION_CALL_CONTROL = "com.info.logifit.plus.pe.call_control"
    }
}