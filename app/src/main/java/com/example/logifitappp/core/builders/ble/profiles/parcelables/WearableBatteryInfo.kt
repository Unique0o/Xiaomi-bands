package com.example.logifitappp.core.builders.ble.profiles.parcelables

import android.os.Parcel
import android.os.Parcelable

class WearableBatteryInfo() : Parcelable {
    var percentCharged: Int = 0

    constructor(parcel: Parcel): this() {
        percentCharged = parcel.readInt()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(percentCharged)
    }

    companion object CREATOR : Parcelable.Creator<WearableBatteryInfo> {
        override fun createFromParcel(parcel: Parcel): WearableBatteryInfo {
            return WearableBatteryInfo(parcel)
        }

        override fun newArray(size: Int): Array<WearableBatteryInfo?> {
            return arrayOfNulls(size)
        }
    }
}