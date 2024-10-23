package com.example.logifitappp.data.repository

import com.example.logifitappp.data.dao.ProvinceDao
import com.example.logifitappp.data.models.ProvinceModel
import com.example.logifitappp.domain.repository.ProvinceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProvinceRepositoryImpl @Inject constructor(
    private val provinceDao: ProvinceDao
) : ProvinceRepository {
    override suspend fun all(departmentExternalIdentifier: Int): List<ProvinceModel> = withContext(Dispatchers.IO) {
        provinceDao.getProvincesByDepartment(departmentExternalIdentifier)
    }

    override suspend fun replaceAll(payloads: List<ProvinceModel>) = withContext(Dispatchers.IO) {
        provinceDao.replaceAll(payloads)
    }
}