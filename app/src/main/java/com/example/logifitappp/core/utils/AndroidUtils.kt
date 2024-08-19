package com.example.logifitappp.core.utils

import android.os.ParcelUuid
import android.os.Parcelable

class AndroidUtils {
    companion object {
        fun toParcelUuids(uuids: Array<Parcelable>?): Array<ParcelUuid>? {
            if (uuids == null) return null

            return Array(uuids.size) { uuids[it] as ParcelUuid }
        }
    }
}