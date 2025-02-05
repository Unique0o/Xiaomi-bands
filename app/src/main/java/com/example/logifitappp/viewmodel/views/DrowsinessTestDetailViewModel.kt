package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.EvaluationResultModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.EvaluationAnswerRequest
import com.example.logifitappp.data.remote.dto.requests.StoreEvaluationRequest
import com.example.logifitappp.domain.service.EvaluationService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.viewmodel.states.DrowsinessTestDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DrowsinessTestDetailViewModel.DrowsinessTestDetailViewModelFactory::class)
class DrowsinessTestDetailViewModel @AssistedInject constructor(
    @Assisted private val drowsinessTestId: Int,
    @Assisted private val user: UserModel?,
    private val evaluationService: EvaluationService
): ViewModel() {
    @AssistedFactory
    interface DrowsinessTestDetailViewModelFactory {
        fun create(drowsinessTestId: Int, user: UserModel?): DrowsinessTestDetailViewModel
    }

    var state by mutableStateOf(DrowsinessTestDetailState())
        private set

    init {
        load()
    }

    private fun checkIfCanSignature(answers: Map<Int, EvaluationAnswerRequest>): Boolean {
        if (state.data == null) return false

        for (question in state.data!!.items) {
            if (question.isRequired != 1) continue

            if (!answers.containsKey(question.id) || answers[question.id]!!.answer.isEmpty()) return false
        }

        return true
    }

    fun fetchTestDetail() {
        viewModelScope.launch {
            try {
                state = state.copy(isFetchingTestDetail = true)

                state = state.copy(
                    data = evaluationService.fetchQuestions(drowsinessTestId),
                    hasFetchTestDetailFailed = false
                )
            } catch (e: Exception) {
                state = state.copy(hasFetchTestDetailFailed = true)
            } finally {
                state = state.copy(isFetchingTestDetail = false)
            }
        }
    }

    fun handleQuestionAnswered(questionId: Int, answer: String) {
        val answers = state.answers.toMutableMap()
        answers[questionId] = answers[questionId]?.copy(answer = answer) ?: EvaluationAnswerRequest(answer = answer, itemToolId = questionId)

        state = state.copy(
            answers = answers,
            canSign = checkIfCanSignature(answers)
        )
    }

    fun handleQuestionDetailChanged(questionId: Int, detail: String) {
        val answers = state.answers.toMutableMap()
        answers[questionId] = answers[questionId]?.copy(detail = detail) ?: EvaluationAnswerRequest(answer = "", detail = detail, itemToolId = questionId)

        state = state.copy(
            answers = answers,
            canSign = checkIfCanSignature(answers)
        )
    }

    private fun load() {
        if (user == null) fetchTestDetail()
        else {
            val evaluation = App.database.evaluationResultDao().findFromToday(drowsinessTestId, user.id)

            if (evaluation == null) fetchTestDetail()
            else state = state.copy(result = evaluation)
        }
    }

    fun moveTo(page: Int) {
        state = state.copy(currentPage = page)
    }

    fun sign(signature: String) {
        state = state.copy(signature = "data:image/png;base64,$signature")
        storeTest()
    }

    fun stopProcessing() {
        state = state.copy(isStoringResult = false)
    }

    private fun storeTest() {
        if (user == null) return

        viewModelScope.launch {
            try {
                state = state.copy(
                    isStoringResult = true,
                    status = AppStatusCodeEnum.STORING_EVALUATION
                )

                val response = evaluationService.store(StoreEvaluationRequest(
                    answers = state.answers.values.toList(),
                    detail_tool_id = drowsinessTestId,
                    id = user.id,
                    signature = state.signature
                ))

                val evaluation = EvaluationResultModel(
                    condition = response.condition,
                    createdAt = DateTimeUtils.parse(response.createdAt, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss"),
                    evaluationId = response.evaluationId,
                    id = response.id,
                    result = response.result,
                    title = state.data?.evaluation?.name ?: "N/A",
                    userId = user.id
                )

                App.database.evaluationResultDao().store(evaluation)

                state = state.copy(
                    currentPage = 0,
                    isStoringResult = false,
                    result = evaluation,
                    shouldItBackUntilHome = true
                )
            } catch (e: Exception) {
                state = state.copy(status = AppStatusCodeEnum.FAILED_EVALUATION_STORE)
            }
        }
    }
}