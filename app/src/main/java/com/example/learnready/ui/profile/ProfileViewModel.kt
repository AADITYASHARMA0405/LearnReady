package com.example.learnready.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val modulesCompleted: Int = 0,
    val averageScore: Int = 0,
    val subjectsCount: Int = 0,
    val selectedLanguage: String = "en",
    val isLoading: Boolean = true,
    val badges: List<Badge> = defaultBadges()
)

enum class BadgeIcon {
    QUIZ, PERFECT, LESSONS, QUICK
}

data class Badge(
    val title: String,
    val description: String,
    val icon: BadgeIcon,
    val isEarned: Boolean
)

fun defaultBadges(): List<Badge> = listOf(
    Badge("First Quiz", "Complete your first quiz", BadgeIcon.QUIZ, false),
    Badge("Perfect Score", "Score 100% on a quiz", BadgeIcon.PERFECT, false),
    Badge("5 Lessons", "Complete 5 lessons", BadgeIcon.LESSONS, false),
    Badge("Quick Learner", "Complete 3 modules", BadgeIcon.QUICK, false)
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: LearnReadyRepository
) : ViewModel() {

    private val userId = "demo_user"

    val uiState: StateFlow<ProfileUiState> = combine(
        repository.getUserFlow(userId),
        repository.getAllProgress(userId)
    ) { user, progressList ->
        val completedProgress = progressList.filter { it.status == "completed" }
        val modulesCompleted = completedProgress.map { it.moduleId }.distinct().size
        val averageScore = if (completedProgress.isNotEmpty()) {
            completedProgress.mapNotNull { it.score }.average().toInt()
        } else 0

        // Compute badge earning
        val badges = listOf(
            Badge("First Quiz", "Complete your first quiz", BadgeIcon.QUIZ, completedProgress.isNotEmpty()),
            Badge("Perfect Score", "Score 100% on a quiz", BadgeIcon.PERFECT, completedProgress.any { (it.score ?: 0) == 100 }),
            Badge("5 Lessons", "Complete 5 lessons", BadgeIcon.LESSONS, progressList.filter { it.lessonId != null }.size >= 5),
            Badge("Quick Learner", "Complete 3 modules", BadgeIcon.QUICK, modulesCompleted >= 3)
        )

        ProfileUiState(
            user = user,
            modulesCompleted = modulesCompleted,
            averageScore = averageScore,
            subjectsCount = completedProgress.map { it.moduleId }.distinct().size,
            selectedLanguage = user?.selectedLanguage ?: "en",
            isLoading = false,
            badges = badges
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ProfileUiState()
    )

    fun toggleLanguage() {
        val user = uiState.value.user ?: return
        val newLang = if (user.selectedLanguage == "en") "kn" else "en"
        viewModelScope.launch {
            repository.updateUser(user.copy(selectedLanguage = newLang))
        }
    }
}
