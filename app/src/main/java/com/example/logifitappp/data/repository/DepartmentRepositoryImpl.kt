package com.example.logifitappp.data.repository

import com.example.logifitappp.data.dao.DepartmentDao
import com.example.logifitappp.data.models.DepartmentModel
import com.example.logifitappp.domain.repository.DepartmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

 class DepartmentRepositoryImpl @Inject constructor(
    private val departmentDao: DepartmentDao
) : DepartmentRepository {
    override suspend fun all(countryExternalIdentifier: Int): List<DepartmentModel> = withContext(Dispatchers.IO) {
        departmentDao.getAllDepartmentsByCountry(countryExternalIdentifier)
    }

    override suspend fun replaceAll(payloads: List<DepartmentModel>) = withContext(Dispatchers.IO) {
        departmentDao.replaceAll(payloads)
    }
}