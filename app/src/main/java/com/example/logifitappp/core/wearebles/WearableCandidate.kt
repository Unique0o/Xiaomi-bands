package com.example.logifitappp.core.wearebles

import android.bluetooth.BluetoothDevice
import android.content.res.Resources
import android.os.Parcel
import android.os.ParcelUuid
import android.os.Parcelable
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.AndroidUtils
import java.lang.reflect.InvocationTargetException
import java.util.UUID

class WearableCandidate() : Parcelable, Cloneable {
    private var isBonded: Boolean? = null
    private var name: String? = null
    private var rssi: Short = 0
    private var services: Array<ParcelUuid>? = null
    private var wearable: BluetoothDevice? = null

    constructor(parcel: Parcel) : this() {
        wearable = parcel.readParcelable(this.javaClass.classLoader) ?: throw IllegalStateException("Unable to read state from Parcel")

        rssi = parcel.readInt().toShort()
        services = AndroidUtils.toParcelUuids(parcel.readParcelableArray(this.javaClass.classLoader))
        name = parcel.readString()

        val bonded = parcel.readInt()

        if (bonded != -1) isBonded = bonded == 1
    }

    constructor(wearable: BluetoothDevice, rssi: Short, services: Array<ParcelUuid>?) : this() {
        this.wearable = wearable
        this.rssi = rssi
        this.services = services ?: arrayOf()
    }

    fun addUuids(wearableServices: Array<ParcelUuid>?) {
        services = mergeServices(services, wearableServices)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WearableCandidate

        return wearable!!.address.equals(other.wearable!!.address)
    }

    fun getDevice(): BluetoothDevice {
        return wearable!!
    }

    fun getMacAddress(): String {
        return wearable?.address ?: Resources.getSystem().getString(R.string.device_unknown_name)
    }

    fun getName(): String {
        if (isNameKnown()) return name!!

        return "(unknown)"
    }

    fun getIsBonded(): Boolean {
        if (isBonded == null) {
            isBonded = try {
                wearable!!.bondState == BluetoothDevice.BOND_BONDING
            } catch (e: SecurityException) {
                false
            }
        }

        return isBonded!!
    }

    fun getRssi(): Short {
        return rssi
    }

    fun getServices(): Array<ParcelUuid>? {
        return services
    }

    override fun hashCode(): Int {
        return wearable!!.address.hashCode() xor 37
    }

    fun isNameKnown(): Boolean {
        return !name.isNullOrEmpty()
    }

    private fun mergeServices(services: Array<ParcelUuid>?, wearableServices: Array<ParcelUuid>?): Array<ParcelUuid> {
        val uuids = linkedSetOf<ParcelUuid>()

        services?.let { uuids.addAll(it.toList())  }
        wearableServices?.let { uuids.addAll(it.toList())  }

        return uuids.toTypedArray()
    }

    fun refreshNameIfUnknown() {
        if (isNameKnown()) return

        try {
            val method = wearable!!.javaClass.getMethod("getAliasName")
            name = method.invoke(wearable!!) as String
        } catch (e: Exception) {
            when (e) {
                is IllegalAccessException,
                is InvocationTargetException -> println("Could not get device alias for ${wearable!!.address}")
            }
        }

        if (name.isNullOrEmpty()) {
            try {
                name = wearable!!.name
            } catch (e: SecurityException) {
                println("SecurityException on device.getName")
            }
        }
    }

    fun setRssi(rssi: Short) {
        this.rssi = rssi
    }

    fun supportsService(serviceUuid: UUID): Boolean {
        val uuids = getServices() ?: return false.also { println("no cached services available for $this") }

        for (uuid in uuids) {
            if (serviceUuid == uuid.uuid) return true
        }

        return false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(wearable, 0)
        parcel.writeInt(rssi.toInt())
        parcel.writeParcelableArray(services, 0)
        parcel.writeString(name)

        if (isBonded == null) parcel.writeInt(-1)
        else parcel.writeInt(if (isBonded!!) 1 else 0)
    }

    companion object CREATOR: Parcelable.Creator<WearableCandidate> {
        override fun createFromParcel(parcel: Parcel): WearableCandidate {
            return WearableCandidate(parcel)
        }

        override fun newArray(size: Int): Array<WearableCandidate?> {
            return arrayOfNulls(size)
        }
    }
}