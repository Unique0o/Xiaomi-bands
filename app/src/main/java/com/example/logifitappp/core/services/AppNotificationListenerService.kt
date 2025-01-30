package com.example.logifitappp.core.services

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.media.session.MediaController
import android.media.session.MediaSession
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.os.Process
import android.provider.MediaStore
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppNotificationMap
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.core.specs.CallSpec
import com.example.logifitappp.core.specs.NotificationSpec
import com.example.logifitappp.core.utils.LimitedQueue
import com.example.logifitappp.core.utils.MediaManager
import com.example.logifitappp.core.utils.NotificationUtils
import com.example.logifitappp.enums.CallSpecTypeEnum
import com.example.logifitappp.enums.NotificationSpecTypeEnum
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class AppNotificationListenerService: NotificationListenerService() {
    private lateinit var notificationPictureCacheDirectory: File

    private val actionLookup = LimitedQueue<Int, NotificationCompat.Action>(32)
    private val notificationHandleLookup = LimitedQueue<Int, Long>(128)
    private val packageLookup = LimitedQueue<Int, String>(64)

    private var activeCallPostTime = 0L
    private var lastCallCommand = CallSpecTypeEnum.CALL_UNDEFINED

    private val handler = Handler(Looper.getMainLooper())
    private var musicRunnable: Runnable? = null
    private var musicStateRunnable: Runnable? = null

    private val notificationBurstPrevention = HashMap<String, Long>()
    private val notificationOldRepeatPrevention = HashMap<String, Long>()

    private val notificationsActive = mutableListOf<Int>()
    private val notificationStack = mutableListOf<String>()

    private val groupSummaryWhitelist = hashSetOf(
        "com.microsoft.office.lync15",
        "com.skype.raider",
        "mikado.bizcalpro"
    )

    private val supportedPictureMimeTypes = hashSetOf(
        "image/",
        "image/jpeg",
        "image/png",
        "image/gif",
        "image/bmp",
        "image/webp"
    )

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: return

            val handle = intent.getLongExtra("handle", -1).toInt()

            when (action) {
                App.ACTION_QUIT -> stopSelf()

                ACTION_OPEN -> {
                    val sbns = this@AppNotificationListenerService.activeNotifications
                    val ts = notificationHandleLookup.lookup(handle)

                    if (ts == null) {
                        println("could not lookup handle for open action")
                        return
                    }

                    for (sbn in sbns) {
                        if (sbn.postTime != ts) continue

                        try {
                            sbn.notification.contentIntent?.send()
                        } catch (e: PendingIntent.CanceledException) {
                            println("Failed to open notification ${sbn.id}")
                        }
                    }
                }

                ACTION_MUTE -> {
                    val packageName = packageLookup.lookup(handle)

                    if (packageName == null) {
                        println("could not lookup handle for mute action")
                        return
                    }

                    println("going to mute $packageName")
                }

                ACTION_DISMISS -> {
                    val sbns = this@AppNotificationListenerService.activeNotifications
                    val ts = notificationHandleLookup.lookup(handle)

                    if (ts == null) {
                        println("could not lookup handle for dismiss action")
                        return
                    }

                    for (sbn in sbns) {
                        if (sbn.postTime != ts) continue

                        this@AppNotificationListenerService.cancelNotification(sbn.key)
                    }
                }

                ACTION_DISMISS_ALL -> this@AppNotificationListenerService.cancelAllNotifications()

                ACTION_REPLY -> {
                    val wearableAction = actionLookup.lookup(handle)
                    val reply = intent.getStringExtra("reply")

                    if (wearableAction == null) {
                        println("Received ACTION_REPLY but cannot find the corresponding wearableAction")
                        return
                    }

                    val actionIntent = wearableAction.actionIntent

                    if (actionIntent == null) {
                        println("Action intent is null")
                        return
                    }

                    val localIntent = Intent().apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }

                    if (!wearableAction.remoteInputs.isNullOrEmpty()) {
                        val remoteInputs = wearableAction.remoteInputs
                        val extras = Bundle().apply { putCharSequence(remoteInputs?.get(0)?.resultKey, reply) }

                        RemoteInput.addResultsToIntent(remoteInputs!!, localIntent, extras)
                    }

                    try {
                        println("will send exec intent to remote application")
                        actionIntent.send(context, 0, localIntent)
                        actionLookup.remove(handle)
                    } catch (e: PendingIntent.CanceledException) {
                        println("replyToLastNotification error $e")
                    }
                }
            }
        }
    }

    private fun checkNotificationContentForWhiteAndBlackList(packageName: String, body: String): Boolean {
        return true
    }

    private fun cleanUpNotificationPictureProvider() {
        notificationPictureCacheDirectory.listFiles()?.forEach {
            it.delete()
        }
    }

    private fun createNotificationPictureCacheDirectory() {
        val cacheDir = applicationContext.externalCacheDir
        notificationPictureCacheDirectory = File(cacheDir, "notification-pictures")
        notificationPictureCacheDirectory.mkdir()
    }

    private fun dissectNotificationTo(notification: Notification, notificationSpec: NotificationSpec, preferBigText: Boolean) {
        val extras = NotificationCompat.getExtras(notification) ?: return

        extras.getCharSequence(Notification.EXTRA_TITLE)?.let {
            notificationSpec.title = sanitizeUnicode(it.toString())
        }

        val bigText = extras.getCharSequence(NotificationCompat.EXTRA_BIG_TEXT)
        val contentCS = when {
            preferBigText && !StringUtils.isBlank(bigText) -> bigText
            extras.containsKey(Notification.EXTRA_TEXT) -> extras.getCharSequence(NotificationCompat.EXTRA_TEXT)
            else -> null
        }

        contentCS?.let { notificationSpec.body = sanitizeUnicode(it.toString()) }

        val messagingStyle = NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(notification)

        if (messagingStyle != null) {
            val messages = messagingStyle.messages

            if (messages.isNotEmpty()) {
                val lastMessage = messages[messages.size - 1]

                if (supportedPictureMimeTypes.contains(lastMessage.dataMimeType)) {
                    val contentResolver = contentResolver

                    try {
                        contentResolver.query(lastMessage.dataUri!!, null, null, null, null).use { cursor ->
                            if (cursor != null && cursor.moveToFirst()) {
                                val dataIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                notificationSpec.picturePath = cursor.getString(dataIndex)
                            }
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            }
        }

        if (extras.containsKey(NotificationCompat.EXTRA_PICTURE)) {
            val bmp = extras[NotificationCompat.EXTRA_PICTURE] as Bitmap?

            val pictureFile = File(this.notificationPictureCacheDirectory, java.lang.String.valueOf(notificationSpec.id))

            try {
                FileOutputStream(pictureFile).use { fos ->
                    bmp?.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    notificationSpec.picturePath = pictureFile.absolutePath
                }
            } catch (e: IOException) {
                println("Failed to save picture to notification cache: ${e.message}")
            } finally {
                bmp?.recycle()
            }
        }

        if (notificationSpec.type === NotificationSpecTypeEnum.COL_REMINDER && notificationSpec.body == null && notificationSpec.title != null) {
            notificationSpec.body = notificationSpec.title
            notificationSpec.title = null
        }
    }

    private fun handleMediaSessionNotification(sbn: StatusBarNotification): Boolean {
        val token = sbn.notification.extras.getParcelable<MediaSession.Token>(Notification.EXTRA_MEDIA_SESSION)
        return token != null && handleMediaSessionNotification(token)
    }

    private fun handleMediaSessionNotification(token: MediaSession.Token): Boolean {
        try {
            val controller = MediaController(applicationContext, token)

            if (controller.metadata == null) return false

            val stateSpec = MediaManager.extractMusicStateSpec(controller.playbackState)
            val musicSpec = MediaManager.extractMusicSpec(controller.metadata)

            musicRunnable?.let { handler.removeCallbacks(it) }
            musicRunnable = Runnable {
                App.wearableService.onSetMusicInfo(musicSpec)
            }

            handler.postDelayed(musicRunnable!!, 100)

            stateSpec?.let { state ->
                musicStateRunnable?.let { handler.removeCallbacks(it) }
                musicStateRunnable = Runnable {
                    App.wearableService.onSetMusicState(state)
                }

                handler.postDelayed(musicStateRunnable!!, 100)
            }

            return true
        } catch (e: NullPointerException) {
            return false
        } catch (e: SecurityException) {
            return false
        }
    }

    private fun isFitnessApp(sbn: StatusBarNotification): Boolean {
        val source = sbn.packageName

        return (source == "de.dennisguse.opentracks"
                || source == "de.dennisguse.opentracks.debug"
                || source == "de.dennisguse.opentracks.nightly"
                || source == "de.dennisguse.opentracks.playstore"
                || source == "de.tadris.fitness"
                || source == "de.tadris.fitness.debug")
    }

    private fun isOutsideNotificationTimes(prefs: AppPreferences): Boolean {
        if (!prefs.getNotificationTimesEnabled()) return false

        val now = LocalTime.now()
        val start = prefs.getNotificationTimesStart()
        val end = prefs.getNotificationTimesEnd()

        return when {
            start.isBefore(end) -> now.isAfter(start) && now.isBefore(end)
            else -> now.isAfter(start) || now.isBefore(end)
        }
    }

    private fun isServiceNotRunningAndShouldIgnoreNotifications(): Boolean {
        if (!WearableCommunicationService.isRunning(this)) return true.also { println("Service is not running, ignoring notification") }

        return false
    }

    private fun isWorkProfile(sbn: StatusBarNotification): Boolean {
        val currentUser = Process.myUserHandle()

        return !sbn.user.equals(currentUser)
    }

    private fun handleCallNotification(sbn: StatusBarNotification) {
        val app = sbn.packageName
        println("got call from: $app")

        if (app == "com.android.dialer" || app == "com.android.incallui" || app == "com.google.android.dialer" || app == "com.asus.asusincallui" || app == "com.samsung.android.incallui") {
            println("Ignoring non-voip call")
            return
        }

        val notification = sbn.notification

        val callStarted = when {
            !notification.actions.isNullOrEmpty() && notification.actions.size == 1 -> run oneNotification@ {
                if (lastCallCommand == CallSpecTypeEnum.CALL_INCOMING) {
                    return@oneNotification true.also { println("There is only one call action and previous state was CALL_INCOMING, assuming call started") }
                }

                println("There is only one call action and previous state was not CALL_INCOMING, assuming outgoing call / duplicate notification and ignoring")
                return
            }

            else -> false
        }

        val appName = NotificationUtils.getApplicationLabel(this, app)
        val number = notification.extras.getString(Notification.EXTRA_PEOPLE) ?: notification.extras.getString(Notification.EXTRA_TITLE) ?: appName ?: app
        activeCallPostTime = sbn.postTime

        val callSpec = CallSpec(if (callStarted) CallSpecTypeEnum.CALL_START else CallSpecTypeEnum.CALL_INCOMING)
        callSpec.number = number
        callSpec.sourceAppId = app
        appName?.let { callSpec.sourceName = appName }

        lastCallCommand = callSpec.command

        App.wearableService.onSetCallState(callSpec)
    }

    override fun onCreate() {
        super.onCreate()

        IntentFilter().apply {
            addAction(App.ACTION_QUIT)
            addAction(ACTION_OPEN)
            addAction(ACTION_DISMISS)
            addAction(ACTION_DISMISS_ALL)
            addAction(ACTION_MUTE)
            addAction(ACTION_REPLY)

            LocalBroadcastManager.getInstance(this@AppNotificationListenerService).registerReceiver(receiver, this)
            createNotificationPictureCacheDirectory()
            cleanUpNotificationPictureProvider()
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
        notificationStack.clear()
        notificationsActive.clear()
        cleanUpNotificationPictureProvider()

        super.onDestroy()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        onNotificationPosted(sbn, null)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        if (sbn == null) return

        notificationStack.remove(sbn.packageName)
        notificationStack.add(sbn.packageName)

        if (isServiceNotRunningAndShouldIgnoreNotifications()) return

        val prefs = App.preferences

        if (isOutsideNotificationTimes(prefs)) return

        val ignoreWorkProfile = prefs.getBoolean("notifications_ignore_work_profile", false)

        if (ignoreWorkProfile && isWorkProfile(sbn)) {
            println("Ignoring notification from work profile")
            return
        }

        val mediaIgnoresAppList = prefs.getBoolean("notification_media_ignores_application_list", false)

        if (mediaIgnoresAppList && handleMediaSessionNotification(sbn)) return

        if (shouldIgnoreSource(sbn)) return

        if (!mediaIgnoresAppList && handleMediaSessionNotification(sbn)) return

        val dndSuppressed = when {
            rankingMap != null -> run rankingNull@ {
                val ranking = Ranking()

                if (rankingMap.getRanking(sbn.key, ranking) && !ranking.matchesInterruptionFilter()) return@rankingNull 1

                return@rankingNull 0
            }

            else -> 0
        }

        if (prefs.getBoolean("notification_filter", false) && dndSuppressed == 1) return

        if (
            NotificationCompat.CATEGORY_CALL == sbn.notification.category &&
            prefs.getBoolean("notification_support_voip_calls", false) &&
            (sbn.isOngoing || shouldDisplayNonOngoingCallNotification(sbn))
        ) {
            handleCallNotification(sbn)
            return
        }

        if (shouldIgnoreNotification(sbn, false) && "com.sec.android.app.clockpackage" != sbn.packageName) return

        val source = sbn.packageName
        val notification = sbn.notification
        val notificationOldRepeatPreventionValue = notificationOldRepeatPrevention[source]

        if (notificationOldRepeatPreventionValue != null && notification.`when` <= notificationOldRepeatPreventionValue && !shouldIgnoreRepeatPrevention(sbn)) {
            println("NOT processing notification, already sent newer notifications from this source.")
            return
        }

        val currentTime = System.nanoTime()
        val notificationBurstPreventionValue = notificationBurstPrevention[source]

        if (notificationBurstPreventionValue != null) {
            val diff = currentTime - notificationBurstPreventionValue

            if (diff < TimeUnit.SECONDS.toNanos(prefs.getInt("notifications_timeout", 0).toLong())) {
                println("Ignoring frequent notification, last one was ${TimeUnit.NANOSECONDS.toMillis(diff)} ms ago")
                return
            }
        }

        val notificationSpec = NotificationSpec().apply {
            key = sbn.key
            `when` = notification.`when`
            sourceName = NotificationUtils.getApplicationLabel(this@AppNotificationListenerService, source)
            sourceAppId = source
            type = AppNotificationMap.getInstance()[source] ?: NotificationSpecTypeEnum.UNKNOWN
        }

        if (source.startsWith("com.fsck.k9")) {
            if (NotificationCompat.isGroupSummary(notification)) {
                println("ignore K9 group summary")
                return
            }
        }

        println("Processing notification ${notificationSpec.id}, age: ${System.currentTimeMillis() - notification.`when`}, source: $source, flags: ${notification.flags}")

        val preferBigText = prefs.getBoolean("notification_prefer_long_text", true)
        dissectNotificationTo(notification, notificationSpec, preferBigText)

        if (notificationSpec.title != null || notificationSpec.body != null) {
            val textToCheck = "${notificationSpec.title ?: ""} ${notificationSpec.body ?: ""}"

            if (!checkNotificationContentForWhiteAndBlackList(sbn.packageName.lowercase(), textToCheck)) return
        }

        if (applicationContext.packageName == source) return

        val wearableExtender = NotificationCompat.WearableExtender(notification)
        val actions = wearableExtender.actions

        if (actions.isEmpty() && NotificationCompat.isGroupSummary(notification) && !groupSummaryWhitelist.contains(source)) {
            println("Not forwarding notification, FLAG_GROUP_SUMMARY is set and no wearable action present. Notification flags: ${notification.flags}")
            return
        }

        notificationSpec.attachedActions = mutableListOf()
        notificationSpec.dndSuppressed = dndSuppressed

        notificationSpec.attachedActions?.add(NotificationSpec.Action().apply {
            title = "Dismiss"
            type = NotificationSpec.Action.TYPE_SYNTECTIC_DISMISS
        })

        for (action in actions) {
            if (action == null) continue

            val wearableAction = NotificationSpec.Action()
            wearableAction.title = action.title.toString()
            wearableAction.type = if (!action.remoteInputs.isNullOrEmpty()) NotificationSpec.Action.TYPE_WEARABLE_REPLY else NotificationSpec.Action.TYPE_WEARABLE_SIMPLE

            notificationSpec.attachedActions?.add(wearableAction)
            wearableAction.handle = ((notificationSpec.id shl 4) + (notificationSpec.attachedActions?.size ?: 0)).toLong()
            actionLookup.add(wearableAction.handle.toInt(), action)

            println("Found wearable action: ${notificationSpec.attachedActions?.size} - ${action.title}  ${sbn.tag}")
        }

        notificationSpec.attachedActions?.add(NotificationSpec.Action().apply {
            title = getString(R.string.open_on_phone)
            type = NotificationSpec.Action.TYPE_SYNTECTIC_OPEN
        })

        notificationSpec.attachedActions?.add(NotificationSpec.Action().apply {
            title = getString(R.string.mute)
            type = NotificationSpec.Action.TYPE_SYNTECTIC_MUTE
        })

        notificationHandleLookup.add(notificationSpec.id, sbn.postTime)
        packageLookup.add(notificationSpec.id, sbn.packageName)
        notificationBurstPrevention[source] = currentTime

        if (notification.`when` == 0L) println("This app might show old/duplicate notifications. notification.when is 0 for $source")
        else if (notification.`when` - System.currentTimeMillis() > 30000L) println("This app might show old/duplicate notifications. notification.when is in the future for $source")
        else notificationOldRepeatPrevention[source] = notification.`when`

        notificationsActive.add(notificationSpec.id)
        App.wearableService.onNotification(notificationSpec)
    }

    private fun sanitizeUnicode(orig: String): String {
        return orig.replace("[\\p{C}&&\\S]".toRegex(), "")
    }

    private fun shouldDisplayNonOngoingCallNotification(sbn: StatusBarNotification): Boolean {
        val source = sbn.packageName
        val type = AppNotificationMap.getInstance()[source]

        return type === NotificationSpecTypeEnum.TELEGRAM
    }

    private fun shouldIgnoreNotification(sbn: StatusBarNotification, remove: Boolean): Boolean {
        val notification = sbn.notification
        val source = sbn.packageName

        val type = AppNotificationMap.getInstance()[source]

        if (NotificationCompat.getLocalOnly(notification) && type !== NotificationSpecTypeEnum.WECHAT && type !== NotificationSpecTypeEnum.TELEGRAM && type !== NotificationSpecTypeEnum.OUTLOOK && type !== NotificationSpecTypeEnum.COL_REMINDER && type !== NotificationSpecTypeEnum.SKYPE) {
            return true.also { println("Ignoring notification, local only") }
        }

        val prefs = App.preferences

        if (!remove) {
            if (!prefs.getBoolean("notifications_generic_whenscreenon", false)) {
                val powerManager = getSystemService(POWER_SERVICE) as PowerManager?

                if (powerManager != null && powerManager.isScreenOn) {
                    return true.also { println("Not forwarding notification, screen seems to be on and settings do not allow this") }
                }
            }
        }

        if (sbn.notification.priority < Notification.PRIORITY_DEFAULT) {
            if (prefs.getBoolean("notifications_ignore_low_priority", true)) {
                return true.also { println("Ignoring low priority notification") }
            }
        }

        if (shouldIgnoreOngoing(sbn, type)) {
            return false
        }

        return (notification.flags and Notification.FLAG_ONGOING_EVENT) == Notification.FLAG_ONGOING_EVENT
    }

    private fun shouldIgnoreOngoing(sbn: StatusBarNotification, type: NotificationSpecTypeEnum?): Boolean {
        if (isFitnessApp(sbn)) return true

        return type === NotificationSpecTypeEnum.COL_REMINDER
    }

    private fun shouldIgnoreRepeatPrevention(sbn: StatusBarNotification): Boolean {
        return isFitnessApp(sbn)
    }

    private fun shouldIgnoreSource(sbn: StatusBarNotification) = when (sbn.packageName) {
        "android", "com.android.systemui", "com.android.dialer", "com.google.android.dialer", "com.cyanogenmod.eleven" ->
            true.also { println("Ignoring notification, is a system event") }

        "com.moez.QKSMS", "com.android.mms", "com.sonyericsson.conversations", "com.android.messaging", "org.smssecure.smssecure" ->
            "never" != App.preferences.getString("notification_mode_sms", "when_screen_off")

        else -> false
    }

    companion object {
        const val ACTION_DISMISS = "com.info.logifit.plus.pe.app.notification.listener.service.action.dismiss"
        const val ACTION_DISMISS_ALL = "com.info.logifit.plus.pe.app.notification.listener.service.action.dismiss_all"
        const val ACTION_MUTE = "com.info.logifit.plus.pe.app.notification.listener.service.action.mute"
        const val ACTION_OPEN = "com.info.logifit.plus.pe.app.notification.listener.service.action.open"
        const val ACTION_REPLY = "com.info.logifit.plus.pe.app.notification.listener.service.action.reply"
    }
}