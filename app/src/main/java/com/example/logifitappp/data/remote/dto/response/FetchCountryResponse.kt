package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.CountryModel

data class CountryResponse(
    val id: Int,
    val name: String
)

data class FetchCountryResponse(
    val data: List<CountryResponse>
)

fun FetchCountryResponse.toCountryModels() = data.map {
    CountryModel(
        id = it.id,
        name = it.name
    )
}