package com.example.logifitappp.core.wearebles

class WearableHelper {
    private val cache = HashMap<String, WearableType>()

    private fun getOrderedDeviceTypes(): Array<WearableType> {
        return WearableType.entries.toTypedArray()
    }

    private fun resolveWearableType(candidate: WearableCandidate): WearableType {
        return resolveWearableType(candidate, true)
    }

    fun resolveWearableType(candidate: WearableCandidate, useCache: Boolean): WearableType {
        synchronized(this) {
            if (useCache) {
                val cachedType = cache.get(candidate.getMacAddress().lowercase())

                if (cachedType != null) return cachedType
            }

            for (type in getOrderedDeviceTypes()) {
                if (type.getWearableCoordinator().supports(candidate)) {
                    cache[candidate.getMacAddress().lowercase()] = type
                    return type
                }
            }

            cache[candidate.getMacAddress().lowercase()] = WearableType.UNKNOWN
        }

        return WearableType.UNKNOWN
    }

    fun getSupportedWearable(candidate: WearableCandidate): Wearable {
        val wearableType = resolveWearableType(candidate)

        return wearableType.getWearableCoordinator().createDevice(candidate, wearableType)
    }

    companion object {
        private var instance = WearableHelper()

        fun getInstance(): WearableHelper {
            return instance
        }
    }
}