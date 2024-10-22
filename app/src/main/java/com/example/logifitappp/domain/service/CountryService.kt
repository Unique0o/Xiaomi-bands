package com.example.logifitappp.domain.service

import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.data.models.DepartmentModel
import com.example.logifitappp.data.models.ProvinceModel
import com.example.logifitappp.domain.repository.CountryRepository
import com.example.logifitappp.domain.repository.DepartmentRepository
import com.example.logifitappp.domain.repository.ProvinceRepository

class CountryService(
    private val locationService: LocationService,
    private val countryRepository: CountryRepository,
    private val departmentRepository: DepartmentRepository,
    private val provinceRepository: ProvinceRepository
) {
    suspend fun update() {
        val countries = locationService.fetchCountries()
        val departments = locationService.fetchDepartments()
        val provinces = locationService.fetchProvinces()

        countryRepository.replaceAll(countries.map { country ->
            CountryModel(
                externalIdentifier = country.id,
                name = country.name
            )
        })

        departmentRepository.replaceAll(departments.map { department ->
            DepartmentModel(
                countryExternalIdentifier = department.countryId,
                externalIdentifier = department.id,
                name = department.name
            )
        })

        provinceRepository.replaceAll(provinces.map { province ->
            ProvinceModel(
                departmentExternalIdentifier = province.departmentId,
                externalIdentifier = province.id,
                name = province.name
            )
        })
    }
}
