package com.example.learnready.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.StudyPlan
import com.example.learnready.data.db.entities.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubjectWithLessons(
    val subject: Subject,
    val lessonCount: Int
)

data class HomeUiState(
    val subjects: List<SubjectWithLessons> = emptyList(),
    val userName: String = "Aaditya",
    val isLoading: Boolean = true,
    // Analytics
    val overallReadiness: Int = 0,
    val modulesCompleted: Int = 0,
    val averageQuizScore: Int = 0,
    val currentStreak: Int = 0,
    val recentQuizScores: List<Int> = emptyList(),
    // Today's Plan
    val todaysPlan: List<StudyPlan> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: LearnReadyRepository
) : ViewModel() {

    private val userId = "demo_user"

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
        loadAnalytics()
        loadStudyPlan()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.getAllSubjects().collect { subjects ->
                val subjectsWithLessons = subjects.map { subject ->
                    val lessonCount = repository.getLessonCountForSubject(subject.id)
                    SubjectWithLessons(subject, lessonCount)
                }
                _uiState.update {
                    it.copy(
                        subjects = subjectsWithLessons,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            repository.getAllProgress(userId).collect { progressList ->
                val completed = progressList.filter { it.status == "completed" }
                val modulesCompleted = completed.map { it.moduleId }.distinct().size
                val scores = completed.mapNotNull { it.score }
                val averageScore = if (scores.isNotEmpty()) scores.average().toInt() else 0

                // Recent quiz scores for chart (last 10)
                val recentScores = completed
                    .filter { it.score != null }
                    .sortedByDescending { it.updatedAt }
                    .take(10)
                    .mapNotNull { it.score }
                    .reversed()

                // Simple streak: count consecutive days with activity
                val streak = calculateStreak(completed)

                // Overall readiness: 40% academic completion + 30% quiz accuracy + 30% (placeholder for skills/interview)
                val completionRate = if (modulesCompleted > 0) {
                    (modulesCompleted * 100) / 9 // 9 total modules in seed data
                } else 0
                val readiness = (completionRate * 0.4 + averageScore * 0.3 + averageScore * 0.3).toInt()
                    .coerceIn(0, 100)

                _uiState.update {
                    it.copy(
                        overallReadiness = readiness,
                        modulesCompleted = modulesCompleted,
                        averageQuizScore = averageScore,
                        currentStreak = streak,
                        recentQuizScores = recentScores
                    )
                }
            }
        }
    }

    private fun loadStudyPlan() {
        viewModelScope.launch {
            // Generate fresh plan
            repository.generateStudyPlan(userId)

            // Observe pending plans
            repository.getPendingStudyPlan(userId).collect { plans ->
                _uiState.update { it.copy(todaysPlan = plans) }
            }
        }
    }

    private fun calculateStreak(completedProgress: List<com.example.learnready.data.db.entities.StudentProgress>): Int {
        if (completedProgress.isEmpty()) return 0

        val today = System.currentTimeMillis()
        val dayMs = 24 * 60 * 60 * 1000L
        val progressDays = completedProgress
            .map { it.updatedAt / dayMs }
            .distinct()
            .sortedDescending()

        val todayDay = today / dayMs
        var streak = 0
        for (i in progressDays.indices) {
            if (progressDays[i] == todayDay - i) {
                streak++
            } else {
                break
            }
        }
        return streak.coerceAtLeast(if (completedProgress.isNotEmpty()) 1 else 0)
    }
}
