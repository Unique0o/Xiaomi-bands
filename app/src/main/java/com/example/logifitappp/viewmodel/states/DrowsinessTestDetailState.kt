package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.data.models.EvaluationResultModel
import com.example.logifitappp.data.remote.dto.requests.EvaluationAnswerRequest
import com.example.logifitappp.data.remote.dto.response.FetchQuestionResponse
import com.example.logifitappp.enums.AppStatusCodeEnum

data class DrowsinessTestDetailState(
    var answers: Map<Int, EvaluationAnswerRequest> = mapOf(),
    var canSign: Boolean = false,
    var currentPage: Int = 0,
    var data: FetchQuestionResponse? = null,
    var hasFetchTestDetailFailed: Boolean = false,
    var isFetchingTestDetail: Boolean = true,
    var isStoringResult: Boolean = false,
    var result: EvaluationResultModel? = null,
    var shouldItBackUntilHome: Boolean = false,
    var signature: String = "",
    var status: AppStatusCodeEnum = AppStatusCodeEnum.STORING_EVALUATION
)
