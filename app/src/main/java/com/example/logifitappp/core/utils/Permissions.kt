package com.example.logifitappp.core.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

object Permissions {

    fun requestPermission(
        context: Context,
        permissions: Array<String>,
        onPermissionsResult: (granted:Boolean)->Unit
    ){

        val isAllGranted=permissions.all { permission->
            ContextCompat.checkSelfPermission(context,permission)==PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted){
            onPermissionsResult(true)
        }else{
            val launcher = (context as? ComponentActivity)?.activityResultRegistry
                ?.register("permissionsKey", ActivityResultContracts.RequestMultiplePermissions()) { results ->
                    val allGranted = results.values.all { it }
                    onPermissionsResult(allGranted)
                }

            launcher?.launch(permissions)
        }
    }
}

