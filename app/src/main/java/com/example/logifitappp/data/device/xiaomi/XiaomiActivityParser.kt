//import com.example.logifitappp.data.device.xiaomi.XiaomiActivityFileId
//import com.example.logifitappp.data.device.xiaomi.XiaomiSupport
//import org.slf4j.LoggerFactory
//
//abstract class XiaomiActivityParser {
//
//     abstract fun parse(support: XiaomiSupport, fileId: XiaomiActivityFileId, bytes: ByteArray): Boolean
//    }
//
//    companion object {
//        private val LOG = LoggerFactory.getLogger(XiaomiActivityParser::class.java)
//
//        fun create(fileId: XiaomiActivityFileId): XiaomiActivityParser? {
//            return when (fileId.type) {
//                XiaomiActivityFileId.Type.ACTIVITY -> createForActivity(fileId)
//                XiaomiActivityFileId.Type.SPORTS -> createForSports(fileId)
//                else -> {
//                    LOG.warn("Unknown file type for {}", fileId)
//                    null
//                }
//            }
//        }
//
//        private fun createForActivity(fileId: XiaomiActivityFileId): XiaomiActivityParser? {
//            require(fileId.type == XiaomiActivityFileId.Type.ACTIVITY)
//            return when (fileId.subtype) {
//                XiaomiActivityFileId.Subtype.ACTIVITY_DAILY -> when (fileId.detailType) {
//                    XiaomiActivityFileId.DetailType.DETAILS -> XiaomiDailyDetailsParser()
//                    else -> null
//                }
//                XiaomiActivityFileId.Subtype.ACTIVITY_SLEEP_STAGES -> when (fileId.detailType) {
//                    XiaomiActivityFileId.DetailType.DETAILS -> SleepStagesParser()
//                    else -> null
//                }
//                XiaomiActivityFileId.Subtype.ACTIVITY_SLEEP -> SleepDetailsParser()
//                else -> null
//            }
//        }
//
//        private fun createForSports(fileId: XiaomiActivityFileId): XiaomiActivityParser? {
//                return null
//            }
//        }
//    }
