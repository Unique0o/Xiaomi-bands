package com.example.logifitappp.core.wearebles

import android.os.Parcel
import android.os.Parcelable

class WearableInfo(): Parcelable {
    var hardwareRevision: String? = null
    var firmwareRevision: String? = null
    var softwareRevision: String? = null

    constructor(parcel: Parcel): this() {
        hardwareRevision = parcel.readString()
        firmwareRevision = parcel.readString()
        softwareRevision = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hardwareRevision)
        parcel.writeString(firmwareRevision)
        parcel.writeString(softwareRevision)
    }

    companion object CREATOR: Parcelable.Creator<WearableInfo> {
        override fun createFromParcel(parcel: Parcel): WearableInfo {
            return WearableInfo(parcel)
        }

        override fun newArray(size: Int): Array<WearableInfo?> {
            return arrayOfNulls(size)
        }
    }
}