package com.example.logifitappp.core.wearebles

import java.util.regex.Pattern
import kotlin.math.max

class WearableVersion(private val version: String?): Comparable<WearableVersion?> {
    init {
        if (version == null) throw IllegalArgumentException("Version can not be null")

        val pattern = Pattern.compile("[0-9]+(\\.[0-9]+)*")

        if (!pattern.matcher(version).matches()) throw IllegalArgumentException("Invalid version format")
    }

    override fun compareTo(other: WearableVersion?): Int {
        if (other == null) return 1

        require(this.version != null)
        require(other.version != null)

        val thisParts = this.version.split("\\.")
        val otherParts = other.version.split("\\.")

        val length = max(thisParts.size, otherParts.size)

        for (i in 0 until length) {
            val thisPart = if (i < thisParts.size) thisParts[i].toInt() else 0
            val otherPart = if (i < otherParts.size) otherParts[i].toInt() else 0

            if (thisPart < otherPart) return -1

            if (thisPart > otherPart) return 1
        }

        return 0
    }
}