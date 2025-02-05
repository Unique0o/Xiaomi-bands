package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.domain.service.DocumentTypeService
import com.example.logifitappp.domain.service.LocationService
import javax.inject.Inject

class UpdatePersonalInformationFormDataUseCase @Inject constructor(
    private val documentTypeService: DocumentTypeService,
    private val locationService: LocationService
) {
    suspend operator fun invoke() {
        val countries = locationService.fetchCountries()
        val departments = locationService.fetchDepartments()
        val provinces = locationService.fetchProvinces()
        val documentTypes = documentTypeService.all()

        App.database.apply {
            countryDao().replaceAll(*countries.toTypedArray())
            departmentDao().replaceAll(*departments.toTypedArray())
            provinceDao().replaceAll(*provinces.toTypedArray())
            documentTypeDao().replaceAll(*documentTypes.toTypedArray())
        }
    }
}