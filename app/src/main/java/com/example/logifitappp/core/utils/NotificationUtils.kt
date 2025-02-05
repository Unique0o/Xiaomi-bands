package com.example.logifitappp.core.utils

import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.os.UserManager
import com.example.logifitappp.core.specs.CallSpec

object NotificationUtils {
    fun getApplicationLabel(context: Context, packageName: String): String? {
        val pm = context.packageManager
        try {
            return pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString()
        } catch (ignored: PackageManager.NameNotFoundException) {
            println("Failed to find application label for {}, attempting fallback $packageName")

            val launcherActivityInfo = getLauncherActivityInfo(context, packageName)

            return launcherActivityInfo?.label?.toString()
        }
    }

    private fun getLauncherActivityInfo(context: Context, packageName: String): LauncherActivityInfo? {
        try {
            val launcherAppsService = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
            val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
            val userProfiles = userManager.userProfiles

            for (i in 1 until userProfiles.size) {
                val userHandle = userProfiles[i]
                val activityList = launcherAppsService.getActivityList(packageName, userHandle)

                if (activityList.isNotEmpty()) {
                    println("Found ${activityList.size} launcher activity infos for $packageName in user $userHandle")
                    return activityList[0]
                }
            }

            println("Failed to find launcher activity info for $packageName")
        } catch (e: Exception) {
            println("Error during launcher activity info search $e")
        }

        return null
    }

    fun getPreferredTextFor(callSpec: CallSpec) = StringUtils.getFirstOf(callSpec.name, callSpec.number)
}