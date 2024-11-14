package com.example.logifitappp.core.wearebles

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.logifitappp.R

class Wearable(): Parcelable {
    private var address: String? = null
    private var alias: String? = null
    private var batteryLevel: IntArray? = intArrayOf(BATTERY_UNKNOWN.toInt(), BATTERY_UNKNOWN.toInt(), BATTERY_UNKNOWN.toInt())
    private var busyTask: String? = null
    private var firmwareVersion: String? = null
    private var model: String? = null
    private var name: String? = null
    private var rssi: Short = RSSI_UNKNOWN
    private var state: State = State.NOT_CONNECTED
    private lateinit var wearableType: WearableTypeEnum

    constructor(parcel: Parcel) : this() {
        this.name = parcel.readString()
        this.alias = parcel.readString()
        this.address = parcel.readString()
        this.wearableType = WearableTypeEnum.entries.toTypedArray()[parcel.readInt()]
        this.firmwareVersion = parcel.readString()
        this.model = parcel.readString()
        this.state = State.entries.toTypedArray()[parcel.readInt()]
        this.batteryLevel = parcel.createIntArray()
        this.rssi = parcel.readInt().toShort()
        this.busyTask = parcel.readString()
    }

    constructor(address: String, name: String?, alias: String?, wearableType: WearableTypeEnum, firmwareVersion: String? = null): this() {
        this.address = address
        this.alias = alias
        this.firmwareVersion = firmwareVersion
        this.name = name ?: address
        this.wearableType = wearableType
    }

    fun copyFromDevice(wearable: Wearable) {
        if (wearable.getAddress() != this.address) throw RuntimeException("Cannot copy from device with other address")

        this.name = wearable.name
        this.alias = wearable.alias
        this.batteryLevel = wearable.batteryLevel
        this.busyTask = wearable.busyTask
        this.firmwareVersion = wearable.firmwareVersion
        this.model = wearable.model
        this.rssi = wearable.rssi
        this.state = wearable.state
        this.wearableType = wearable.wearableType
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true

        if (other !is Wearable) return false

        return other.getAddress().equals(this.address)
    }

    fun getAddress(): String? {
        return address
    }

    fun getAliasOrName(): String {
        if (!alias.isNullOrEmpty()) return alias!!

        return name ?: "unknown"
    }

    fun getBatteryLevel() = batteryLevel?.get(0) ?: 0

    fun getBusyTask(): String? {
        return busyTask
    }

    fun getFirmwareVersion(): String? {
        return firmwareVersion
    }

    fun getName(): String? {
        return name
    }

    fun getState(): State {
        return state
    }

    fun getType() = wearableType

    fun getWearableCoordinator(): WearableCoordinator {
        return wearableType.getWearableCoordinator()
    }

    fun getWearableType(): WearableTypeEnum {
        return wearableType
    }

    override fun hashCode(): Int {
        return address.hashCode() xor 37
    }

    fun isBusy(): Boolean {
        return busyTask != null
    }

    fun isConnected(): Boolean {
        return state == State.SCANNED || state.equalsOrHigherThan(State.CONNECTED)
    }

    fun isConnecting(): Boolean {
        return state == State.CONNECTING
    }

    fun isDisconnected() = state == State.NOT_CONNECTED

    fun isInitialized(): Boolean {
        return state == State.SCANNED || state.equalsOrHigherThan(State.INITIALIZED)
    }

    fun sendDeviceUpdateIntent(context: Context) {
        sendDeviceUpdateIntent(context, WearableUpdateSubjectEnum.UNKNOWN)
    }

    fun sendDeviceUpdateIntent(context: Context, subject: WearableUpdateSubjectEnum) {
        val wearableUpdateIntent = Intent(ACTION_DEVICE_CHANGED)
        wearableUpdateIntent.putExtra(EXTRA_DEVICE, this)
        wearableUpdateIntent.putExtra(EXTRA_UPDATE_SUBJECT, subject)
        LocalBroadcastManager.getInstance(context).sendBroadcast(wearableUpdateIntent)
    }

    fun setBatteryLevel(batteryLevel: Int, index: Int) {
        if ((batteryLevel in 0..100) || batteryLevel.toShort() == BATTERY_UNKNOWN) {
            this.batteryLevel?.set(index, batteryLevel)
        } else println("Battery level musts be within range 0-100: $batteryLevel")
    }

    fun setBusyTask(task: String?) {
        if (task == null) throw IllegalArgumentException("busy task must not be null")

        if (busyTask != null) println("Attempt to mark device as busy with: $task, but is already busy with: $busyTask")

        println("Mark device as busy: $task")
        busyTask = task
    }

    fun setFirmwareVersion(firmwareVersion: String?) {
        this.firmwareVersion = firmwareVersion
    }

    fun setModel(model: String?) {
        this.model = model
    }

    private fun setRssi(rssi: Short) {
        this.rssi = rssi
    }

    fun setState(state: State) {
        this.state = state

        if (state.ordinal <= State.CONNECTED.ordinal) unsetDynamicState()
    }

    override fun toString(): String {
        return "wearable: {name=$name, mac=$address, type=${wearableType.name}, state=${state.name}, isSupported=${wearableType.isSupported()}}"
    }

    fun unsetBusyTask() {
        if (busyTask == null) {
            println("Attempt to mark device as not busy anymore, but was not busy before.")
            return
        }

        println("Mark device as NOT busy anymore: $busyTask")
        busyTask = null
    }

    private fun unsetDynamicState() {
        setBatteryLevel(BATTERY_UNKNOWN.toInt(), 0)
        setBatteryLevel(BATTERY_UNKNOWN.toInt(), 1)
        setBatteryLevel(BATTERY_UNKNOWN.toInt(), 2)
        setFirmwareVersion(null)
        setRssi(RSSI_UNKNOWN)
        unsetBusyTask()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(alias)
        parcel.writeString(address)
        parcel.writeInt(wearableType.ordinal)
        parcel.writeString(firmwareVersion)
        parcel.writeString(model)
        parcel.writeInt(state.ordinal)
        parcel.writeIntArray(batteryLevel)
        parcel.writeInt(rssi.toInt())
        parcel.writeString(busyTask)
    }

    companion object CREATOR : Parcelable.Creator<Wearable> {
        const val ACTION_DEVICE_CHANGED = "com.info.logifit.pe.action.device_changed"
        const val BATTERY_UNKNOWN: Short = -1
        const val EXTRA_DEVICE = "device"
        const val EXTRA_UPDATE_SUBJECT = "EXTRA_UPDATE_SUBJECT"
        const val RSSI_UNKNOWN: Short = 0

        override fun createFromParcel(parcel: Parcel): Wearable {
            return Wearable(parcel)
        }

        override fun newArray(size: Int): Array<Wearable?> {
            return arrayOfNulls(size)
        }
    }

    enum class State(private val identifier: Int, private val simpleIdentifier: Int) {
        NOT_CONNECTED(R.string.not_connected),
        WAITING_FOR_RECONNECT(R.string.waiting_for_reconnect),
        WAITING_FOR_SCAN(R.string.waiting_for_device_scan),
        SCANNED(R.string.scanned),
        CONNECTING(R.string.connecting),
        CONNECTED(R.string.connected, R.string.connecting),
        INITIALIZING(R.string.initializing, R.string.connecting),
        AUTHENTICATION_REQUIRED(R.string.authentication_required),
        AUTHENTICATING(R.string.authenticating),
        INITIALIZED(R.string.initialized, R.string.connected);

        constructor (identifier: Int) : this(identifier, identifier)

        fun equalsOrHigherThan(other: State): Boolean {
            return compareTo(other) >= 0
        }

        fun getIdentifier(): Int {
            return identifier
        }

        fun getSimpleIdentifier(): Int {
            return simpleIdentifier
        }
    }
}