package com.example.logifitappp.core.wearebles

import android.os.Parcel
import android.os.Parcelable
import com.example.logifitappp.R

class Wearable() : Parcelable {
    private var address: String? = null
    private var alias: String? = null
    private var batteryLevel: IntArray? = intArrayOf(BATTERY_UNKNOWN.toInt(), BATTERY_UNKNOWN.toInt(), BATTERY_UNKNOWN.toInt())
    private var busyTask: String? = null
    private var firmwareVersion: String? = null
    private var model: String? = null
    private var name: String? = null
    private var rssi: Short = RSSI_UNKNOWN
    private var state: State = State.NOT_CONNECTED
    private lateinit var wearableType: WearableType

    constructor(parcel: Parcel) : this() {
        this.name = parcel.readString()
        this.alias = parcel.readString()
        this.address = parcel.readString()
        this.wearableType = WearableType.entries.toTypedArray()[parcel.readInt()]
        this.firmwareVersion = parcel.readString()
        this.model = parcel.readString()
        this.state = State.entries.toTypedArray()[parcel.readInt()]
        this.batteryLevel = parcel.createIntArray()
        this.rssi = parcel.readInt().toShort()
        this.busyTask = parcel.readString()
    }

    constructor(address: String, name: String?, alias: String?, wearableType: WearableType): this() {
        this.address = address
        this.alias = alias
        this.name = name ?: address
        this.wearableType = wearableType
    }

    override fun describeContents(): Int {
        return 0
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
        const val BATTERY_UNKNOWN: Short = -1
        const val EXTRA_DEVICE = "device"
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