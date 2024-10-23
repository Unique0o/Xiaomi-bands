package com.example.logifitappp.data.repository

import com.example.logifitappp.data.dao.ProvinceDao
import com.example.logifitappp.data.models.ProvinceModel

class ProvinceRepositoryImpl(private val provinceDao: ProvinceDao) {
    suspend fun all(departmentExternalIdentifier: Int): List<ProvinceModel> {
        return provinceDao.getProvincesByDepartment(departmentExternalIdentifier)
    }

    suspend fun replaceAll(payloads: List<ProvinceModel>) {
        provinceDao.clearAllProvinces()
        provinceDao.insertProvinces(payloads)
    }
}