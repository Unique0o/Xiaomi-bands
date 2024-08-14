//package com.example.logifitappp.data.device.xiaomi
//
//import android.content.Context
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.example.logifitappp.R
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.util.*
//
//abstract class WearableOperator(val context: Context, val wearable: Wearable) {
//    var isConnected = false
//        private set
//
//    private val _batteryInfo = MutableLiveData<WearableBatteryInfo>()
//    val batteryInfo: LiveData<WearableBatteryInfo> = _batteryInfo
//
//    private val _connectionStatus = MutableLiveData<Boolean>()
//    val connectionStatus: LiveData<Boolean> = _connectionStatus
//
//    open fun canCalculateFatigue(): Boolean = false
//
//    suspend fun deleteActivityEntitiesToFreeStorage() {
//        withContext(Dispatchers.IO) {
//            val activityProvider = getWearableActivityProvider()
//            val to = Calendar.getInstance().apply {
//                add(Calendar.DAY_OF_MONTH, -3)
//                set(Calendar.HOUR_OF_DAY, 23)
//                set(Calendar.MINUTE, 59)
//                set(Calendar.SECOND, 59)
//            }
//            activityProvider.deleteAllBefore(to.timeInMillis / 1000)
//        }
//    }
//
//    fun disconnect() {
//        signalAboutDisconnectedDevice()
//    }
//
//    suspend fun force2021Protocol(): Boolean {
//        return withContext(Dispatchers.IO) {
//            SettingService.fetch(AppSettingKeyEnum.PROTOCOL_STATUS_STORE_ITEM, AppSettingModelEnum.WEARABLE, wearable.id) == "true"
//        }
//    }
//
//    suspend fun getActivitiesBetweenADay(date: Calendar): List<WearableRawActivityModel> {
//        return withContext(Dispatchers.IO) {
//            val (startOfDay, endOfDay) = getStartAndEndOfDay(date)
//            getActivitiesBetweenTimestamp(startOfDay.toString(), (endOfDay - 1).toString())
//        }
//    }
//
//    suspend fun getActivitiesBetweenTimestamp(from: String, to: String): List<WearableRawActivityModel> {
//        return withContext(Dispatchers.IO) {
//            val activityProvider = getWearableActivityProvider()
//            activityProvider.fetchBetween(from, to)
//        }
//    }
//
//    open suspend fun getDeviceSpecificSettingsCustomizer(): WearableSpecificSettingsCustomizer? = null
//
//    fun getHeartRateMeasurementIntervals(): Map<Int, String> {
//        return mapOf(
//            0 to context.getString(R.string.off),
//            60 to context.getString(R.string.interval_one_minute),
//            300 to context.getString(R.string.interval_five_minutes),
//            600 to context.getString(R.string.interval_ten_minutes),
//            1800 to context.getString(R.string.interval_thirty_minutes),
//            3600 to context.getString(R.string.interval_one_hour)
//        )
//    }
//
//    protected suspend fun getLocale(): String {
//        var locale = SettingService.fetchByWearableOperator(DeviceSettingPreferenceTypeEnum.PREF_LANGUAGE, this)
//            ?: DeviceSettingPreferenceValueEnum.PREF_LANGUAGE_AUTO
//        if (locale.isBlank() || locale == DeviceSettingPreferenceValueEnum.PREF_LANGUAGE_AUTO) {
//            locale = Locale.getDefault().language
//        }
//        return locale
//    }
//
//    suspend fun getRawDataFromLast24h(): List<WearableRawActivityModel> {
//        return withContext(Dispatchers.IO) {
//            val activityProvider = getWearableActivityProvider()
//            val to = Calendar.getInstance()
//            val from = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }
//            activityProvider.fetchRawBetween(from.timeInMillis / 1000, to.timeInMillis / 1000)
//        }
//    }
//
//    open suspend fun getSleepStagesFromLast24h(): List<XiaomiRawSleepTimeModel> = emptyList()
//
//    open fun getSupportedDeviceSpecificSettings(): List<PreferenceSetting> = emptyList()
//
//    suspend fun getTimeFormat(): String {
//        val timeFormat = SettingService.fetchByWearableOperator(DeviceSettingPreferenceTypeEnum.PREF_TIME_FORMAT, this)
//            ?: DeviceSettingPreferenceValueEnum.PREF_TIME_FORMAT_AUTO
//        return when {
//            timeFormat == DeviceSettingPreferenceValueEnum.PREF_TIME_FORMAT_AUTO ->
//                if (android.text.format.DateFormat.is24HourFormat(context))
//                    DeviceSettingPreferenceValueEnum.PREF_TIME_FORMAT_24H
//                else
//                    DeviceSettingPreferenceValueEnum.PREF_TIME_FORMAT_12H
//            else -> timeFormat
//        }
//    }
//
//    suspend fun getWearableActivityProvider(): WearableActivityProvider<WearableRawActivityModel> {
//        return withContext(Dispatchers.IO) {
//            val wearable = WearableService.createOrFindToAuthenticatedUser(this@WearableOperator)
//            getActivityProvider(wearable.id)
//        }
//    }
//
//    fun handleOnBatteryChange(batteryInfo: WearableBatteryInfo) {
//        _batteryInfo.postValue(batteryInfo)
//    }
//
//    open fun isAllowedForUnpair(): Boolean = true
//
//    suspend fun processPostAuthentication() {
//        withContext(Dispatchers.Main) {
//            isConnected = true
//            _connectionStatus.value = true
//            startMonitoringDeviceDisconnected()
//        }
//    }
//
//    protected fun signalAboutChangeAlias(alias: String) {
//        // Use EventBus or similar to broadcast this event
//    }
//
//    protected fun signalAboutDisconnectedDevice() {
//        CoroutineScope(Dispatchers.Main).launch {
//            isConnected = false
//            _connectionStatus.value = false
//            // Use EventBus or similar to broadcast this event
//        }
//    }
//
//    fun signalAboutFailedAuthentication(error: Throwable? = null) {
//        // Use EventBus or similar to broadcast this event
//    }
//
//    fun signalAboutFinishedAuthentication() {
//        // Use EventBus or similar to broadcast this event
//    }
//
//    fun signalAboutFailedFetchingActivityData(error: Throwable? = null) {
//        // Use EventBus or similar to broadcast this event
//    }
//
//    fun signalAboutFinishedFetchingActivityData() {
//        // Use EventBus or similar to broadcast this event
//    }
//
//    fun signalAboutFinishedHeartRateMeasurement(heartRateMeasurement: Int) {
//        // Use EventBus or similar to broadcast this event
//    }
//
//    fun setWearableAlias(alias: String?) {
//        wearable.alias = alias
//        // Update wearable in database or preferences
//        if (alias != null) {
//            signalAboutChangeAlias(alias)
//        }
//    }
//
//    protected fun startMonitoringDeviceDisconnected() {
//        // Implement Bluetooth disconnection monitoring
//    }
//
//    abstract suspend fun authenticate()
//    abstract suspend fun fetchActivityData()
//    protected abstract fun getActivityProvider(wearableInternalIdentifier: Int): WearableActivityProvider<WearableRawActivityModel>
//    abstract suspend fun notifySettingChanged(preference: DeviceSettingPreferenceTypeEnum)
//    abstract suspend fun onEnableHeartRateSleepSupport(enable: Boolean)
//    abstract suspend fun onSetHeartRateMeasurementInterval(seconds: Int)
//    abstract suspend fun sendVibrationNotification(callSpec: CallSpec)
//    abstract suspend fun setCurrentTimeWithService()
//    abstract suspend fun stopVibrationNotification()
//
//    private fun getStartAndEndOfDay(date: Calendar): Pair<Long, Long> {
//        val startOfDay = date.clone() as Calendar
//        startOfDay.set(Calendar.HOUR_OF_DAY, 0)
//        startOfDay.set(Calendar.MINUTE, 0)
//        startOfDay.set(Calendar.SECOND, 0)
//        startOfDay.set(Calendar.MILLISECOND, 0)
//
//        val endOfDay = startOfDay.clone() as Calendar
//        endOfDay.add(Calendar.DAY_OF_MONTH, 1)
//
//        return Pair(startOfDay.timeInMillis / 1000, endOfDay.timeInMillis / 1000)
//    }
//}