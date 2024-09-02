package com.example.logifitappp.core.utils

import android.os.ParcelUuid
import android.os.Parcelable

object AndroidUtils {
    fun toParcelUuids(uuids: Array<Parcelable>?): Array<ParcelUuid>? {
        if (uuids == null) return null

        return Array(uuids.size) { uuids[it] as ParcelUuid }
    }
}