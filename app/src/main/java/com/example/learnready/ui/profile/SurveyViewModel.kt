package com.example.learnready.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.SurveyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

data class SurveyQuestion(
    val id: String,
    val textEn: String,
    val textKn: String
)

data class SurveyUiState(
    val questions: List<SurveyQuestion> = emptyList(),
    val responses: Map<String, Int> = emptyMap(),
    val isSubmitting: Boolean = false,
    val isComplete: Boolean = false
)

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val repository: LearnReadyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SurveyUiState())
    val uiState: StateFlow<SurveyUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        val tamQuestions = listOf(
            SurveyQuestion("tam_1", "The app is easy to navigate.", "ಅಪ್ಲಿಕೇಶನ್ ನ್ಯಾವಿಗೇಟ್ ಮಾಡಲು ಸುಲಭವಾಗಿದೆ."),
            SurveyQuestion("tam_2", "The app helps me learn faster.", "ವೇಗವಾಗಿ ಕಲಿಯಲು ಅಪ್ಲಿಕೇಶನ್ ನನಗೆ ಸಹಾಯ ಮಾಡುತ್ತದೆ."),
            SurveyQuestion("tam_3", "The practice quizzes are useful.", "ಅಭ್ಯಾಸ ರಸಪ್ರಶ್ನೆಗಳು ಉಪಯುಕ್ತವಾಗಿವೆ."),
            SurveyQuestion("tam_4", "I feel more confident for interviews.", "ಸಂದರ್ಶನಗಳ ಬಗ್ಗೆ ನಾನು ಹೆಚ್ಚು ವಿಶ್ವಾಸ ಹೊಂದಿದ್ದೇನೆ."),
            SurveyQuestion("tam_5", "I would recommend this app to a friend.", "ನಾನು ಈ ಅಪ್ಲಿಕೇಶನ್ ಅನ್ನು ಸ್ನೇಹಿತರಿಗೆ ಶಿಫಾರಸು ಮಾಡುತ್ತೇನೆ.")
        )
        
        _uiState.value = _uiState.value.copy(
            questions = tamQuestions,
            responses = tamQuestions.associate { it.id to 0 } // 0 means unanswered
        )
    }

    fun updateResponse(questionId: String, value: Int) {
        val currentResponses = _uiState.value.responses.toMutableMap()
        currentResponses[questionId] = value
        _uiState.value = _uiState.value.copy(responses = currentResponses)
    }

    fun submitSurvey() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true)
            
            val jsonObject = JSONObject()
            _uiState.value.responses.forEach { (key, value) ->
                jsonObject.put(key, value)
            }
            
            val response = SurveyResponse(
                userId = "demo_user",
                surveyType = "tam",
                responsesJson = jsonObject.toString()
            )
            
            repository.insertSurveyResponse(response)
            
            _uiState.value = _uiState.value.copy(
                isSubmitting = false,
                isComplete = true
            )
        }
    }
}
