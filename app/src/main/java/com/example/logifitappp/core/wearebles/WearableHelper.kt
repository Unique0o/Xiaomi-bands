package com.example.logifitappp.core.wearebles

import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.WearableModel

class WearableHelper {
    private val cache = HashMap<String, WearableTypeEnum>()

    fun getAvailableWearables() = LinkedHashSet(getStoredWearables())

    private fun getOrderedDeviceTypes(): Array<WearableTypeEnum> {
        return WearableTypeEnum.entries.toTypedArray()
    }

    private fun getStoredWearables(): List<Wearable> {
        val result = mutableListOf<Wearable>()
        val user = App.database.userDao().getLoggedIn() ?: return result

        App.database.wearableDao().all(user.id).forEach {
            val wearable = toSupportedDevice(it)

            if (wearable.getType().isSupported()) result.add(wearable)
        }

        return result
    }

    fun getSupportedWearable(candidate: WearableCandidate): Wearable {
        val wearableType = resolveWearableType(candidate)

        return wearableType.getWearableCoordinator().createDevice(candidate, wearableType)
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

    private fun toSupportedDevice(model: WearableModel): Wearable {
        return Wearable(model.mac, model.name, model.alias, WearableTypeEnum.fromName(model.typeName), model.firmwareVersion)
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