package com.example.logifitappp.core.wearebles

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import com.example.logifitappp.core.App
import com.example.logifitappp.core.handlers.WearableEventHandler
import com.example.logifitappp.core.services.WearableCommunicationService
import com.example.logifitappp.core.specs.CallSpec
import com.example.logifitappp.core.specs.MusicSpec
import com.example.logifitappp.core.specs.MusicStateSpec
import com.example.logifitappp.core.specs.NotificationSpec

open class WearableService(private val context: Context, private val wearable: Wearable?): WearableEventHandler {
    private var serviceClass: Class<out Service> = WearableCommunicationService::class.java

    constructor(context: Context): this(context, null)

    fun connect() {
        connect(false)
    }

    fun connect(firstTime: Boolean) {
        invokeService(createIntent().setAction(ACTION_CONNECT).putExtra(EXTRA_CONNECT_FIRST_TIME, firstTime))
    }

    private fun createIntent(): Intent {
        return Intent(context, serviceClass)
    }

    fun disconnect() {
        invokeService(createIntent().setAction(ACTION_DISCONNECT))
    }

    @SuppressLint("Range")
    private fun getContactDisplayNameByNumber(number: String?): String? {
        if (number.isNullOrEmpty()) return number

        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.ENTERPRISE_CONTENT_FILTER_URI, Uri.encode(number))
        var name = number

        try {
            context.contentResolver.query(uri, null, null, null, null).use { contactLookup ->
                if (contactLookup != null && contactLookup.count > 0) {
                    contactLookup.moveToNext()
                    name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                }
            }
        } catch (e: SecurityException) {

        }

        return name
    }

    private fun invokeService(intent: Intent) {
        if (wearable != null) intent.putExtra(Wearable.EXTRA_DEVICE, wearable)

        try {
            context.startService(intent)
        } catch (e: IllegalStateException) {
            println("IllegalStateException during startService (${intent.action})")
        }
    }

    fun forDevice(wearable: Wearable): WearableService {
        return WearableService(context, wearable)
    }

    override fun onDeleteNotification(id: Int) {
        invokeService(createIntent().setAction(ACTION_DELETE_NOTIFICATION).putExtra(EXTRA_NOTIFICATION_ID, id))
    }

    override fun onFetchRecordedData(dataTypes: Int) {
        invokeService(createIntent().setAction(ACTION_FETCH_RECORDED_DATA).putExtra(EXTRA_RECORDED_DATA_TYPES, dataTypes))
    }

    override fun onNotification(notificationSpec: NotificationSpec) {
        val messagePrivacyMode = App.preferences.getString("pref_message_privacy_mode", "off")
        val hideMessageDetails = messagePrivacyMode == "complete"
        val hideMessageBodyOnly = messagePrivacyMode == "bodyonly"

        createIntent().apply {
            setAction(ACTION_NOTIFICATION)
            putExtra(EXTRA_NOTIFICATION_FLAGS, notificationSpec.flags)
            putExtra(EXTRA_NOTIFICATION_PHONE_NUMBER, if (hideMessageDetails) null else notificationSpec.phoneNumber)
            putExtra(EXTRA_NOTIFICATION_SENDER, if (hideMessageDetails) null else notificationSpec.sender ?: getContactDisplayNameByNumber(notificationSpec.phoneNumber))
            putExtra(EXTRA_NOTIFICATION_SUBJECT, if (hideMessageDetails) null else notificationSpec.subject)
            putExtra(EXTRA_NOTIFICATION_TITLE, if (hideMessageDetails) null else notificationSpec.title)
            putExtra(EXTRA_NOTIFICATION_BODY, if (hideMessageDetails || hideMessageBodyOnly) null else notificationSpec.body)
            putExtra(EXTRA_NOTIFICATION_ID, notificationSpec.id)
            putExtra(EXTRA_NOTIFICATION_KEY, notificationSpec.key)
            putExtra(EXTRA_NOTIFICATION_TYPE, notificationSpec.type)
            putExtra(EXTRA_NOTIFICATION_ACTIONS, notificationSpec.attachedActions?.toTypedArray())
            putExtra(EXTRA_NOTIFICATION_SOURCE_NAME, notificationSpec.sourceName)
            putExtra(EXTRA_NOTIFICATION_SOURCE_APP_ID, notificationSpec.sourceAppId)
            putExtra(EXTRA_NOTIFICATION_PICTURE_PATH, notificationSpec.picturePath)
            putExtra(EXTRA_NOTIFICATION_DND_SUPPRESSED, notificationSpec.dndSuppressed)

            invokeService(this)
        }
    }

    override fun onSetCallState(callSpec: CallSpec) {
        when (App.preferences.getString("pref_call_privacy_mode", "off")) {
            "name" -> callSpec.name = callSpec.number

            "complete" -> {
                callSpec.number = null
                callSpec.name = null
            }

            "number" -> {
                callSpec.name = callSpec.name ?: getContactDisplayNameByNumber(callSpec.number)

                if (callSpec.name != null && !callSpec.name.equals(callSpec.number)) callSpec.number = null
            }

            else -> callSpec.name = callSpec.name ?:  getContactDisplayNameByNumber(callSpec.number)
        }

        createIntent().apply {
            setAction(ACTION_CALL_STATE)
            putExtra(EXTRA_CALL_PHONE_NUMBER, callSpec.number)
            putExtra(EXTRA_CALL_DISPLAY_NAME, callSpec.name)
            putExtra(EXTRA_CALL_SOURCE_NAME, callSpec.sourceName)
            putExtra(EXTRA_CALL_SOURCE_APP_ID, callSpec.sourceAppId)
            putExtra(EXTRA_CALL_COMMAND, callSpec.command.ordinal)
            putExtra(EXTRA_CALL_DND_SUPPRESSED, callSpec.dndSuppressed)

            invokeService(this)
        }
    }

    override fun onSetMusicInfo(musicSpec: MusicSpec?) {
        createIntent().apply {
            setAction(ACTION_SET_MUSIC_INFO)
            putExtra(EXTRA_MUSIC_ARTIST, musicSpec?.artist)
            putExtra(EXTRA_MUSIC_ALBUM, musicSpec?.album)
            putExtra(EXTRA_MUSIC_TRACK, musicSpec?.track)
            putExtra(EXTRA_MUSIC_DURATION, musicSpec?.duration)
            putExtra(EXTRA_MUSIC_TRACK_COUNT, musicSpec?.trackCount)
            putExtra(EXTRA_MUSIC_TRACK_NR, musicSpec?.trackNr)

            invokeService(this)
        }
    }

    override fun onSetMusicState(stateSpec: MusicStateSpec) {
        createIntent().apply {
            setAction(ACTION_SET_MUSIC_STATE)
            putExtra(EXTRA_MUSIC_REPEAT, stateSpec.repeat)
            putExtra(EXTRA_MUSIC_RATE, stateSpec.playRate)
            putExtra(EXTRA_MUSIC_STATE, stateSpec.state)
            putExtra(EXTRA_MUSIC_SHUFFLE, stateSpec.shuffle)
            putExtra(EXTRA_MUSIC_POSITION, stateSpec.position)

            invokeService(this)
        }
    }

    companion object {
        private const val PREFIX = "com.info.logifit.plus.pe.wearables"

        const val ACTION_CALL_STATE = "${PREFIX}.action.call_state"
        const val ACTION_CONNECT = "${PREFIX}.action.connect"
        const val ACTION_DELETE_NOTIFICATION = "${PREFIX}.action.delete_notification"
        const val ACTION_DISCONNECT = "${PREFIX}.action.disconnect"
        const val ACTION_FETCH_RECORDED_DATA = "${PREFIX}.action.fetch_activity_data"
        const val ACTION_NOTIFICATION = "${PREFIX}.action.notification"
        const val ACTION_SET_MUSIC_INFO = "${PREFIX}.action.set_music_info"
        const val ACTION_SET_MUSIC_STATE = "${PREFIX}.action.set_music_state"

        const val EXTRA_CALL_COMMAND = "call_command"
        const val EXTRA_CALL_DISPLAY_NAME = "call_display_name"
        const val EXTRA_CALL_DND_SUPPRESSED = "call_dnd_suppressed"
        const val EXTRA_CALL_PHONE_NUMBER = "call_phone_number"
        const val EXTRA_CALL_SOURCE_APP_ID = "call_source_app_id"
        const val EXTRA_CALL_SOURCE_NAME = "call_source_name"
        const val EXTRA_CONNECT_FIRST_TIME = "connect_first_time"
        const val EXTRA_MUSIC_ALBUM = "music_album"
        const val EXTRA_MUSIC_ARTIST = "music_artist"
        const val EXTRA_MUSIC_DURATION = "music_duration"
        const val EXTRA_MUSIC_POSITION = "music_position"
        const val EXTRA_MUSIC_RATE = "music_rate"
        const val EXTRA_MUSIC_REPEAT = "music_repeat"
        const val EXTRA_MUSIC_SHUFFLE = "music_shuffle"
        const val EXTRA_MUSIC_STATE = "music_state"
        const val EXTRA_MUSIC_TRACK = "music_track"
        const val EXTRA_MUSIC_TRACK_COUNT = "music_track_count"
        const val EXTRA_MUSIC_TRACK_NR = "music_track_nr"
        const val EXTRA_NOTIFICATION_ACTIONS = "notification_actions"
        const val EXTRA_NOTIFICATION_BODY = "notification_body"
        const val EXTRA_NOTIFICATION_DND_SUPPRESSED = "notification_dnd_suppressed"
        const val EXTRA_NOTIFICATION_FLAGS = "notification_flags"
        const val EXTRA_NOTIFICATION_ID = "notification_id"
        const val EXTRA_NOTIFICATION_KEY = "notification_key"
        const val EXTRA_NOTIFICATION_PHONE_NUMBER = "notification_phone_number"
        const val EXTRA_NOTIFICATION_PICTURE_PATH = "notification_picture_path"
        const val EXTRA_NOTIFICATION_SENDER = "notification_sender"
        const val EXTRA_NOTIFICATION_SOURCE_APP_ID = "notification_source_app_id"
        const val EXTRA_NOTIFICATION_SOURCE_NAME = "notification_source_name"
        const val EXTRA_NOTIFICATION_SUBJECT = "notification_subject"
        const val EXTRA_NOTIFICATION_TITLE = "notification_title"
        const val EXTRA_NOTIFICATION_TYPE = "notification_type"
        const val EXTRA_RECORDED_DATA_TYPES = "data_types"
    }
}