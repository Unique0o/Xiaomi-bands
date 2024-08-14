//import com.example.logifitappp.data.device.xiaomi.XiaomiActivityFileId
//import com.example.logifitappp.data.device.xiaomi.XiaomiComplexActivityParser
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import org.slf4j.LoggerFactory
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//import java.util.*
//
//class XiaomiDailyDetailsParser : AbstractXiaomiActivityParser() {
//
//    suspend fun parse(support: XiaomiSupport, fileId: XiaomiActivityFileId, bytes: ByteArray): Boolean {
//        LOG.debug("daily details parser - file: $fileId payload: ${bytes.contentToString()}")
//
//        val version = fileId.version
//        val headerSize = when (version) {
//            1, 2 -> 4
//            3 -> 5
//            else -> {
//                LOG.warn("Unable to parse daily details version ${fileId.version}")
//                return false
//            }
//        }
//
//        val buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
//        buf.position(buf.position() + 7) // skip fileId bytes
//        val fileIdPadding = buf.get()
//        if (fileIdPadding.toInt() != 0) {
//            LOG.warn("Expected 0 padding after fileId, got $fileIdPadding - parsing might fail")
//        }
//
//        val header = ByteArray(headerSize)
//        buf.get(header)
//
//        LOG.debug("Daily Details Header: ${header.joinToString("") { "%02x".format(it) }}")
//
//        val complexParser = XiaomiComplexActivityParser(header, buf)
//        var timestamp = Calendar.getInstance().apply {
//            time = fileId.timestamp
//        }
//
//        val activities = mutableListOf<WearableRawDataType<XiaomiRawActivityModel>>()
//        val activityProvider = support.getWearableActivityProvider() as XiaomiActivityProvider
//
//        while (buf.position() < buf.limit() - 4) {
//            var includeExtraEntry = 0
//
//            val activity = WearableRawDataType<XiaomiRawActivityModel>(
//                heart_rate = 0,
//                intensity = 0,
//                steps = 0,
//                timestamp = timestamp.timeInMillis / 1000,
//                type = ActivityTypeEnum.TYPE_ACTIVITY
//            )
//
//            complexParser.reset()
//
//            if (complexParser.nextGroup(16)) {
//                if (complexParser.hasSecond()) includeExtraEntry = complexParser.get(1, 1)
//                if (complexParser.hasThird()) activity.steps = complexParser.get(2, 14)
//            }
//
//            if (complexParser.nextGroup(8)) {
//                if (complexParser.hasSecond()) {
//                    val calories = complexParser.get(2, 6)
//                    // TODO: Use calories
//                }
//            }
//
//            complexParser.nextGroup(8)
//            complexParser.nextGroup(16)
//
//            if (complexParser.nextGroup(8)) {
//                if (complexParser.hasFirst()) activity.heart_rate = complexParser.get(0, 8)
//            }
//
//            if (complexParser.nextGroup(8)) {
//                if (complexParser.hasFirst()) {
//                    // Energy
//                }
//            }
//
//            complexParser.nextGroup(16)
//
//            if (version >= 3) {
//                if (complexParser.nextGroup(8)) {
//                    if (complexParser.hasFirst()) activity.spo2 = complexParser.get(0, 8)
//                }
//
//                if (complexParser.nextGroup(8)) {
//                    if (complexParser.hasFirst()) {
//                        val stress = complexParser.get(0, 8)
//                        if (stress != 255) activity.stress = stress
//                    }
//                }
//            }
//
//            if (includeExtraEntry == 1) {
//                complexParser.nextGroup(8)
//            }
//
//            activities.add(activityProvider.create(activity))
//            timestamp.add(Calendar.MINUTE, 1)
//        }
//
//        withContext(Dispatchers.IO) {
//            activityProvider.store(activities)
//        }
//
//        return true
//    }
//
//    companion object {
//        private val LOG = LoggerFactory.getLogger(XiaomiDailyDetailsParser::class.java)
//    }
//}