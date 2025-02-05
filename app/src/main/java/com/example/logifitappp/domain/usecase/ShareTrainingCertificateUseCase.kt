package com.example.logifitappp.domain.usecase

import android.content.Context
import com.example.logifitappp.R
import com.example.logifitappp.core.utils.AndroidUtils
import com.example.logifitappp.core.utils.SharingUtils
import com.example.logifitappp.domain.service.TrainingService
import javax.inject.Inject

class ShareTrainingCertificateUseCase @Inject constructor(
    private val trainingService: TrainingService
) {
    suspend operator fun invoke(context: Context, trainingId: Int) {
        val filename = "training_certificate_${trainingId}.pdf"
        val fileInCache = AndroidUtils.getFileInCache(context, filename)

        if (fileInCache != null) {
            SharingUtils.share(context, fileInCache, "application/pdf", R.string.share_training_certificate_message)
            return
        }

        SharingUtils.share(
            context,
            trainingService.download(trainingId),
            filename,
            R.string.share_training_certificate_message
        )
    }
}