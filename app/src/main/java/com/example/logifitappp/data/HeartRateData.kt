package com.example.logifitappp.data

data class HeartRateData(
     val date: String,
     val minRate: Int,
     val maxRate: Int,
     val timeRange: String,
     val ranges: List<Pair<Int, Int>>
)

