package com.example.logifitappp.data.repository

import com.example.logifitappp.data.dao.DepartmentDao
import com.example.logifitappp.data.models.DepartmentModel

class DepartmentRepositoryImpl(private val departmentDao: DepartmentDao) {

    suspend fun all(countryExternalIdentifier: Int): List<DepartmentModel> {
        return departmentDao.getAllDepartmentsByCountry(countryExternalIdentifier)
    }

    suspend fun replaceAll(payloads: List<DepartmentModel>) {
        departmentDao.clearAllDepartments()
        departmentDao.insertDepartments(payloads)
    }
}