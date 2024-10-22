package com.example.logifitappp.domain.service

import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.domain.repository.CountryRepository
import javax.inject.Inject

class CountryService @Inject constructor(
    private val locationService: LocationService,
    private val countryRepository: CountryRepository
) {
    suspend fun update() {
        val countries = locationService.fetchCountries()
        countryRepository.replaceAll(countries.map { country ->
            CountryModel(
                externalIdentifier = country.id,
                name = country.name
            )
        })
    }
}
