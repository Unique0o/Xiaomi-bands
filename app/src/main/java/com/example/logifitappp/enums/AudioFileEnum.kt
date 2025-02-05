package com.example.logifitappp.enums

import android.content.ContentResolver
import android.net.Uri
import androidx.annotation.RawRes
import androidx.compose.ui.graphics.Color
import com.example.logifitappp.R

enum class AudioFileEnum(@RawRes val file: Int, val label: String, val code: String, val gradientList: List<Color>) {
    ADI_GOLDSTEIN_WAVEPOOL_AUDIO(
        R.raw.adi_goldstein_wavepool,
        "Adi Goldstein",
        "adi_goldstein_wavepool_audio",
        listOf(Color(0xfff72585), Color(0xff480ca8), Color(0xff4cc9f0))
    ),

    MASTER_MINDED_DEEP_DIVE_AUDIO(
        R.raw.master_minded_deep_dive,
        "Master Minded Deep Dive",
        "master_minded_deep_dive_audio",
        listOf(Color(0xff03045e), Color(0xff00b4d8), Color(0xffcaf0f8))
    ),

    MASTER_MINDED_OPENING_UP_AUDIO(
        R.raw.master_minded_opening_up,
        "Master Minded Opening Up",
        "master_minded_opening_up_audio",
        listOf(Color(0xff001219), Color(0xffe9d8a6), Color(0xff9b2226))
    ),

    MASTER_MINDED_SURRENDER_AUDIO(
        R.raw.master_minded_surrender,
        "Master Minded Surrender",
        "master_minded_surrender_audio",
        listOf(Color(0xffb7094c), Color(0xff5c4d7d), Color(0xff0091ad))
    ),

    OKAYA_TEXTURES_AUDIO(
        R.raw.okaya_textures,
        "Okaya textures",
        "okaya_textures_audio",
        listOf(Color(0xff11151c), Color(0xff364156), Color(0xffd66853))
    );

    fun geLocalAudioUri(): Uri {
        return Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE).path(file.toString()).build()
    }
}