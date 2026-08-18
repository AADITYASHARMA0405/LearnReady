package com.example.learnready.ui.mock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.InterviewQuestion
import com.example.learnready.data.db.entities.MockInterviewSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

data class QuestionResponse(
    val questionId: Int,
    val answerText: String = "",
    val communicationScore: Int = 0,
    val confidenceScore: Int = 0,
    val technicalScore: Int = 0
)

data class MockInterviewUiState(
    val isLoading: Boolean = true,
    val questions: List<InterviewQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val responses: List<QuestionResponse> = emptyList(),
    val currentAnswer: String = "",
    val currentCommScore: Int = 3,
    val currentConfScore: Int = 3,
    val currentTechScore: Int = 3,
    val phase: InterviewPhase = InterviewPhase.ANSWERING,
    val isComplete: Boolean = false,
    val overallScore: Float = 0f,
    val avgCommunication: Float = 0f,
    val avgConfidence: Float = 0f,
    val avgTechnical: Float = 0f
)

enum class InterviewPhase {
    ANSWERING,  // User types their answer
    RATING,     // User self-rates on rubric
    COMPLETE    // All questions done
}

@HiltViewModel
class MockInterviewViewModel @Inject constructor(
    private val repository: LearnReadyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MockInterviewUiState())
    val uiState: StateFlow<MockInterviewUiState> = _uiState.asStateFlow()

    fun startInterview(questionCount: Int = 5) {
        viewModelScope.launch {
            _uiState.value = MockInterviewUiState(isLoading = true)
            val questions = repository.getRandomInterviewQuestions(questionCount)
            _uiState.value = MockInterviewUiState(
                isLoading = false,
                questions = questions,
                responses = emptyList(),
                phase = InterviewPhase.ANSWERING
            )
        }
    }

    fun updateAnswer(text: String) {
        _uiState.value = _uiState.value.copy(currentAnswer = text)
    }

    fun updateCommScore(score: Int) {
        _uiState.value = _uiState.value.copy(currentCommScore = score)
    }

    fun updateConfScore(score: Int) {
        _uiState.value = _uiState.value.copy(currentConfScore = score)
    }

    fun updateTechScore(score: Int) {
        _uiState.value = _uiState.value.copy(currentTechScore = score)
    }

    fun submitAnswer() {
        // Move from ANSWERING to RATING phase
        _uiState.value = _uiState.value.copy(
            phase = InterviewPhase.RATING,
            currentCommScore = 3,
            currentConfScore = 3,
            currentTechScore = 3
        )
    }

    fun submitRating() {
        val state = _uiState.value
        val response = QuestionResponse(
            questionId = state.questions[state.currentIndex].id,
            answerText = state.currentAnswer,
            communicationScore = state.currentCommScore,
            confidenceScore = state.currentConfScore,
            technicalScore = state.currentTechScore
        )
        val newResponses = state.responses + response
        val nextIndex = state.currentIndex + 1

        if (nextIndex >= state.questions.size) {
            // All done — calculate scores
            val avgComm = newResponses.map { it.communicationScore }.average().toFloat()
            val avgConf = newResponses.map { it.confidenceScore }.average().toFloat()
            val avgTech = newResponses.map { it.technicalScore }.average().toFloat()
            val overall = (avgComm + avgConf + avgTech) / 3f

            _uiState.value = state.copy(
                responses = newResponses,
                isComplete = true,
                phase = InterviewPhase.COMPLETE,
                overallScore = overall,
                avgCommunication = avgComm,
                avgConfidence = avgConf,
                avgTechnical = avgTech
            )

            // Save session to DB
            saveSession(newResponses, overall, avgComm, avgConf, avgTech)
        } else {
            _uiState.value = state.copy(
                responses = newResponses,
                currentIndex = nextIndex,
                currentAnswer = "",
                phase = InterviewPhase.ANSWERING
            )
        }
    }

    private fun saveSession(
        responses: List<QuestionResponse>,
        overall: Float,
        comm: Float,
        conf: Float,
        tech: Float
    ) {
        viewModelScope.launch {
            val jsonArray = JSONArray()
            responses.forEach { r ->
                val obj = JSONObject()
                obj.put("questionId", r.questionId)
                obj.put("answerText", r.answerText)
                obj.put("commScore", r.communicationScore)
                obj.put("confScore", r.confidenceScore)
                obj.put("techScore", r.technicalScore)
                jsonArray.put(obj)
            }
            val session = MockInterviewSession(
                userId = "demo_user",
                completedAt = System.currentTimeMillis(),
                overallScore = overall,
                communicationScore = comm,
                confidenceScore = conf,
                technicalScore = tech,
                responsesJson = jsonArray.toString()
            )
            repository.insertInterviewSession(session)
        }
    }
}
