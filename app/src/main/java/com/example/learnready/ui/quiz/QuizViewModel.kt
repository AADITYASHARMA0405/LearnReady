package com.example.learnready.ui.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.Question
import com.example.learnready.data.db.entities.StudentProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

data class QuizUiState(
    val allQuestions: List<Question> = emptyList(),
    val askedQuestions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: Map<Int, String> = emptyMap(),
    val isChecking: Boolean = false, // True when showing bottom sheet explanation
    val isSubmitted: Boolean = false,
    val score: Int = 0,
    val timeRemainingSeconds: Long = 30 * 60L,
    val isLoading: Boolean = true,
    val moduleName: String = ""
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: LearnReadyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val moduleId: Int = savedStateHandle.get<Int>("moduleId") ?: 0

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
        startTimer()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            val module = repository.getModuleById(moduleId)
            repository.getQuestionsForModule(moduleId).collect { questions ->
                val firstQuestion = questions.filter { it.difficulty == 1 }.randomOrNull() ?: questions.firstOrNull()
                _uiState.update {
                    it.copy(
                        allQuestions = questions,
                        askedQuestions = if (firstQuestion != null) listOf(firstQuestion) else emptyList(),
                        isLoading = false,
                        moduleName = module?.titleEn ?: "Quiz"
                    )
                }
            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_uiState.value.timeRemainingSeconds > 0 && !_uiState.value.isSubmitted) {
                delay(1000L)
                _uiState.update {
                    it.copy(timeRemainingSeconds = it.timeRemainingSeconds - 1)
                }
            }
            if (!_uiState.value.isSubmitted && _uiState.value.timeRemainingSeconds <= 0) {
                submitQuiz()
            }
        }
    }

    fun selectAnswer(questionIndex: Int, answer: String) {
        if (_uiState.value.isSubmitted || _uiState.value.isChecking) return
        _uiState.update {
            it.copy(selectedAnswers = it.selectedAnswers + (questionIndex to answer))
        }
    }

    fun checkAnswer() {
        val state = _uiState.value
        if (state.selectedAnswers.containsKey(state.currentQuestionIndex)) {
            _uiState.update { it.copy(isChecking = true) }
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        val currentQ = state.askedQuestions[state.currentQuestionIndex]
        val selected = state.selectedAnswers[state.currentQuestionIndex]
        val isCorrect = selected == currentQ.correctAnswer

        // Adaptive logic:
        val targetDifficulty = if (isCorrect) currentQ.difficulty + 1 else maxOf(1, currentQ.difficulty - 1)
        
        // Find next question that hasn't been asked yet
        val available = state.allQuestions.filter { q -> !state.askedQuestions.any { it.id == q.id } }
        
        if (available.isEmpty() || state.askedQuestions.size >= 10) {
            // End of quiz (max 10 questions or no more available)
            submitQuiz()
            return
        }

        // Try to find one with target difficulty, else fallback
        var nextQ = available.filter { it.difficulty == targetDifficulty }.randomOrNull()
        if (nextQ == null) {
            nextQ = available.random()
        }

        _uiState.update {
            it.copy(
                askedQuestions = it.askedQuestions + nextQ,
                currentQuestionIndex = it.currentQuestionIndex + 1,
                isChecking = false
            )
        }
    }

    fun submitQuiz() {
        val state = _uiState.value
        if (state.isSubmitted) return

        var score = 0
        state.askedQuestions.forEachIndexed { index, question ->
            val selected = state.selectedAnswers[index]
            if (selected != null && selected == question.correctAnswer) {
                score++
            }
        }

        _uiState.update {
            it.copy(isSubmitted = true, score = score)
        }

        viewModelScope.launch {
            val scorePercent = if (state.askedQuestions.isNotEmpty()) {
                (score * 100) / state.askedQuestions.size
            } else 0

            repository.insertProgress(
                StudentProgress(
                    userId = "demo_user",
                    moduleId = moduleId,
                    lessonId = null,
                    status = "completed",
                    score = scorePercent,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun parseOptions(optionsJson: String): List<String> {
        return try {
            val arr = JSONArray(optionsJson)
            (0 until arr.length()).map { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
