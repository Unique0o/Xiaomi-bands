package com.example.logifitappp.core.wearebles

class WearableHelper {
    private val cache = HashMap<String, WearableTypeEnum>()

    private fun getOrderedDeviceTypes(): Array<WearableTypeEnum> {
        return WearableTypeEnum.entries.toTypedArray()
    }

    fun resolveWearableType(candidate: WearableCandidate): WearableTypeEnum {
        return resolveWearableType(candidate, true)
    }

    fun resolveWearableType(candidate: WearableCandidate, useCache: Boolean): WearableTypeEnum {
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

            cache[candidate.getMacAddress().lowercase()] = WearableTypeEnum.UNKNOWN
        }

        return WearableTypeEnum.UNKNOWN
    }

    fun getSupportedWearable(candidate: WearableCandidate): Wearable {
        val wearableType = resolveWearableType(candidate)

        return wearableType.getWearableCoordinator().createDevice(candidate, wearableType)
    }

    fun toSupportedDevice(candidate: WearableCandidate): Wearable {
        val resolvedType = resolveWearableType(candidate)
        return resolvedType.getWearableCoordinator().createDevice(candidate, resolvedType)
    }

    companion object {
        private var instance = WearableHelper()

        fun getInstance(): WearableHelper {
            return instance
        }
    }
}