package com.example.logifitappp.data.repository

import com.example.logifitappp.domain.repository.OccupationalInfoRepository
import com.example.logifitappp.ui.screens.occupationalInfo.OccupationalInfoItem
import javax.inject.Inject

class OccupationalInfoRepositoryImpl @Inject constructor() : OccupationalInfoRepository {
    override suspend fun getOccupationalInfo(): List<OccupationalInfoItem> {
        return listOf(
            OccupationalInfoItem("Compañía", "LOGIFIT"),
            OccupationalInfoItem("Grupo", "No asignado"),
            OccupationalInfoItem("Turno", "No seleccionado"),
            OccupationalInfoItem("Posición de trabajo", "No asignado"),
            OccupationalInfoItem("Función", "No asignado"),
            OccupationalInfoItem("Tiempo de viaje", "No seleccionado"),
            OccupationalInfoItem("Carga de trabajo", "No seleccionado"),
            OccupationalInfoItem("Tipo de atención ocupacional", "No seleccionado"),
            OccupationalInfoItem("Frecuencia de descansos", "No seleccionado"),
            OccupationalInfoItem("Duración de descansos", "No seleccionado")
        )
    }
}