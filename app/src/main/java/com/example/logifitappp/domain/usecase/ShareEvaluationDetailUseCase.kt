package com.example.logifitappp.domain.usecase

import android.content.Context
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.AndroidUtils
import com.example.logifitappp.core.utils.SharingUtils
import com.example.logifitappp.data.models.EvaluationResultModel
import com.example.logifitappp.domain.service.EvaluationService
import javax.inject.Inject

class ShareEvaluationDetailUseCase @Inject constructor(
    private val evaluationService: EvaluationService
) {
    suspend operator fun invoke(context: Context, evaluation: EvaluationResultModel) {
        val filename = "evaluation_result_${evaluation.id}.pdf"
        val fileInCache = AndroidUtils.getFileInCache(context, filename)

        if (fileInCache != null) {
            SharingUtils.share(context, fileInCache, "application/pdf", R.string.share_evaluation_result_message)
            return
        }

        SharingUtils.share(
            context,
            evaluationService.fetchSpecificResult(evaluation.id),
            filename,
            R.string.share_evaluation_result_message
        )
    }
}