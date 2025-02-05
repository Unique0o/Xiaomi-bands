package com.example.logifitappp.core.wearebles.xiaomi.parsers

import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityDetailTypeEnum
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityFileId
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivitySubtypeEnum
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityTypeEnum
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSupport

abstract class XiaomiActivityParser {
    abstract fun parse(support: XiaomiSupport, fileId: XiaomiActivityFileId, bytes: ByteArray): Boolean

    companion object {
        fun create(fileId: XiaomiActivityFileId): XiaomiActivityParser? = when (fileId.getType()) {
            XiaomiActivityTypeEnum.ACTIVITY -> createForActivity(fileId)
            XiaomiActivityTypeEnum.SPORTS -> createForSports(fileId)

            else -> {
                println("Unknown file type for $fileId")
                null
            }
        }

        private fun createForActivity(fileId: XiaomiActivityFileId): XiaomiActivityParser? = when (fileId.getSubtype()) {
            XiaomiActivitySubtypeEnum.ACTIVITY_DAILY -> when (fileId.getDetailType()) {
                XiaomiActivityDetailTypeEnum.DETAILS -> XiaomiDailyDetailsParser()
                else -> null
            }

            XiaomiActivitySubtypeEnum.ACTIVITY_SLEEP_STAGES -> when (fileId.getDetailType()) {
                XiaomiActivityDetailTypeEnum.DETAILS -> XiaomiSleepStagesParser()
                else -> null
            }

            XiaomiActivitySubtypeEnum.ACTIVITY_SLEEP -> XiaomiSleepDetailsParser()
            else -> null
        }

        private fun createForSports(fileId: XiaomiActivityFileId): XiaomiActivityParser? = null
    }
}