package com.example.logifitappp.viewmodel.views.tests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.data.models.QuestionModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FatigueSelfAssessmentViewModel : ViewModel() {

    private val _questions = MutableStateFlow<List<QuestionModel>>(emptyList())
    val questions: StateFlow<List<QuestionModel>> = _questions

    private val _answers = MutableStateFlow<Map<Int, String>>(emptyMap())
    val answers: StateFlow<Map<Int, String>> = _answers

    private val testQuestions = listOf(
        "test_question_1",
        "test_question_2",
        "test_question_3",
        "test_question_4",
        "test_question_5",
        "test_question_6",
        "test_question_7",
        "test_question_8"
    )

    private val translations = mapOf(
        "test_question_1" to R.string.test_question_1,
        "test_question_2" to R.string.test_question_2,
        "test_question_3" to R.string.test_question_3,
        "test_question_4" to R.string.test_question_4,
        "test_question_5" to R.string.test_question_5,
        "test_question_6" to R.string.test_question_6,
        "test_question_7" to R.string.test_question_7,
        "test_question_8" to R.string.test_question_8
    )

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            val loadedQuestions = testQuestions.mapIndexed { index, question ->
                QuestionModel(
                    id = index + 1,
                    description = question,
                    evaluationExternalIdentifier = 1001,
                    externalIdentifier = index + 1,
                    isRequired = true,
                    type = "options"
                )
            }

            _questions.value = loadedQuestions
        }
    }

    fun updateAnswer(questionId: Int, selectedOption: String) {
        viewModelScope.launch {
            _answers.value = _answers.value.toMutableMap().apply {
                put(questionId, selectedOption)
            }
        }
    }

    fun getTranslatedQuestionResourceId(question: QuestionModel): Int {
        return translations[question.description] ?: R.string.without_data
    }
}
