package com.example.logifitappp.core.wearebles.huami

import com.example.logifitappp.enums.NotificationSpecTypeEnum

object HuamiIcon {
    const val ALARM_CLOCK = 10.toByte()
    const val APP_11 = 11.toByte()
    const val APP_18 = 18.toByte()
    const val CALENDAR = 21.toByte()
    const val CHAT_BLUE_13 = 13.toByte()
    const val CHINESE_15 = 15.toByte()
    const val CHINESE_16 = 16.toByte()
    const val CHINESE_19 = 19.toByte()
    const val CHINESE_20 = 20.toByte()
    const val CHINESE_32 = 32.toByte()
    const val CHINESE_33 = 33.toByte()
    const val CHINESE_9 = 9.toByte()
    const val COW_14 = 14.toByte()
    const val EMAIL = 34.toByte()
    const val FACEBOOK = 3.toByte()
    const val FACEBOOK_MESSENGER = 22.toByte()
    const val HANGOUTS = 30.toByte()
    const val HR_WARNING_36 = 36.toByte()
    const val INSTAGRAM = 12.toByte()
    const val KAKAOTALK = 26.toByte()
    const val LINE = 24.toByte()
    const val MI_31 = 31.toByte()
    const val MI_APP_5 = 5.toByte()
    const val MI_CHAT_2 = 2.toByte()
    const val PENGUIN_1 = 1.toByte()
    const val POKEMONGO = 29.toByte()
    const val RED_WHITE_FIRE_8 = 8.toByte()
    const val SKYPE = 27.toByte()
    const val SNAPCHAT = 6.toByte()
    const val STAR_17 = 17.toByte()
    const val TELEGRAM = 25.toByte()
    const val TWITTER = 4.toByte()
    const val VIBER = 23.toByte()
    const val VKONTAKTE = 28.toByte()
    const val WEATHER = 35.toByte()
    const val WECHAT = 0.toByte()
    const val WHATSAPP = 7.toByte()

    fun mapToIconId(type: NotificationSpecTypeEnum?) = when (type) {
        NotificationSpecTypeEnum.UNKNOWN, NotificationSpecTypeEnum.GENERIC_NAVIGATION -> APP_11

        NotificationSpecTypeEnum.CONVERSATIONS, NotificationSpecTypeEnum.RIOT,
        NotificationSpecTypeEnum.HIPCHAT, NotificationSpecTypeEnum.KONTALK,
        NotificationSpecTypeEnum.ANTOX, NotificationSpecTypeEnum.GENERIC_SMS, NotificationSpecTypeEnum.WECHAT -> WECHAT

        NotificationSpecTypeEnum.GENERIC_EMAIL, NotificationSpecTypeEnum.GMAIL,
        NotificationSpecTypeEnum.YAHOO_MAIL, NotificationSpecTypeEnum.OUTLOOK -> EMAIL

        NotificationSpecTypeEnum.GENERIC_CALENDAR, NotificationSpecTypeEnum.BUSINESS_CALENDAR -> CALENDAR
        NotificationSpecTypeEnum.FACEBOOK -> FACEBOOK
        NotificationSpecTypeEnum.FACEBOOK_MESSENGER, NotificationSpecTypeEnum.SIGNAL -> FACEBOOK_MESSENGER
        NotificationSpecTypeEnum.GOOGLE_HANGOUTS, NotificationSpecTypeEnum.GOOGLE_MESSENGER -> HANGOUTS
        NotificationSpecTypeEnum.INSTAGRAM, NotificationSpecTypeEnum.GOOGLE_PHOTOS -> INSTAGRAM
        NotificationSpecTypeEnum.KAKAO_TALK -> KAKAOTALK
        NotificationSpecTypeEnum.LINE -> LINE
        NotificationSpecTypeEnum.WIRE, NotificationSpecTypeEnum.THREEMA -> CHAT_BLUE_13
        NotificationSpecTypeEnum.TWITTER -> TWITTER
        NotificationSpecTypeEnum.SKYPE -> SKYPE
        NotificationSpecTypeEnum.SNAPCHAT -> SNAPCHAT
        NotificationSpecTypeEnum.TELEGRAM -> TELEGRAM
        NotificationSpecTypeEnum.VIBER, NotificationSpecTypeEnum.DISCORD -> VIBER
        NotificationSpecTypeEnum.WHATSAPP -> WHATSAPP
        NotificationSpecTypeEnum.GENERIC_ALARM_CLOCK -> ALARM_CLOCK
        else -> APP_11
    }
}