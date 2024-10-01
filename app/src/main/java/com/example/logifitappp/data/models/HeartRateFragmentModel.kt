package com.example.logifitappp.data.models

data class HeartRateFragmentModel(
    val endAt: String,
    val latest: Int,
    val max: Int,
    val min: Int,
    val startAt: String
)

typealias HeartRateFragmentGroupType = Map<Long, HeartRateFragmentModel>