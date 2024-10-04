package com.example.logifitappp.data.repository

import android.content.Context
import com.example.logifitappp.R
import com.example.logifitappp.data.models.OccupationalInfoItem
import com.example.logifitappp.domain.repository.OccupationalInfoRepository
import javax.inject.Inject

class OccupationalInfoRepositoryImpl @Inject constructor(
    private val context: Context
) : OccupationalInfoRepository {

    override suspend fun getOccupationalInfo(): List<OccupationalInfoItem> {
        return listOf(
            OccupationalInfoItem(context.getString(R.string.company), "LOGIFIT"),
            OccupationalInfoItem(context.getString(R.string.group), context.getString(R.string.not_assigned)),
            OccupationalInfoItem(context.getString(R.string.shift), context.getString(R.string.not_selected)),
            OccupationalInfoItem(context.getString(R.string.work_position), context.getString(R.string.not_assigned)),
            OccupationalInfoItem(context.getString(R.string.function), context.getString(R.string.not_assigned)),
            OccupationalInfoItem(context.getString(R.string.travel_time), context.getString(R.string.not_selected)),
            OccupationalInfoItem(context.getString(R.string.workload), context.getString(R.string.not_selected)),
            OccupationalInfoItem(context.getString(R.string.occupational_attention_type), context.getString(R.string.not_selected)),
            OccupationalInfoItem(context.getString(R.string.breaks_frequency), context.getString(R.string.not_selected)),
            OccupationalInfoItem(context.getString(R.string.breaks_length), context.getString(R.string.not_selected))
        )
    }
}