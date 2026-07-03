package com.example.learnready.ui.skills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.SkillCategory
import com.example.learnready.data.db.entities.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class SubjectProgress(
    val subject: Subject,
    val progressPercent: Int
)


data class RecentActivity(
    val title: String,
    val timestamp: Long,
    val type: String
)

data class SkillsUiState(
    val overallReadiness: Int = 0,
    val subjectProgress: List<SubjectProgress> = emptyList(),
    val skillCategories: List<SkillCategory> = emptyList(),
    val recentActivity: List<RecentActivity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class SkillsViewModel @Inject constructor(
    private val repository: LearnReadyRepository
) : ViewModel() {

    private val userId = "demo_user"

    val uiState: StateFlow<SkillsUiState> = combine(
        repository.getAllSubjects(),
        repository.getAllProgress(userId),
        repository.getAllSkillCategories()
    ) { subjects, progressList, categories ->
        val completedProgress = progressList.filter { it.status == "completed" }

        // Per-subject progress
        val subjectProgressList = subjects.map { subject ->
            val subjectModuleProgress = completedProgress.filter {
                // We'll estimate by checking module IDs — if the progress has scores
                true // Include all for now
            }
            val moduleIds = completedProgress.map { it.moduleId }.distinct()
            // Rough estimate: percentage based on completed modules vs total expected
            val percent = if (moduleIds.isNotEmpty()) {
                val subjectScores = completedProgress.mapNotNull { it.score }
                if (subjectScores.isNotEmpty()) subjectScores.average().toInt() else 0
            } else 0

            SubjectProgress(
                subject = subject,
                progressPercent = percent.coerceIn(0, 100)
            )
        }

        // Overall readiness
        val overallReadiness = if (subjectProgressList.isNotEmpty()) {
            subjectProgressList.map { it.progressPercent }.average().toInt()
        } else 0

        // Recent activity
        val recentActivities = completedProgress
            .sortedByDescending { it.updatedAt }
            .take(3)
            .map { progress ->
                RecentActivity(
                    title = "Module ${progress.moduleId} completed",
                    timestamp = progress.updatedAt,
                    type = if (progress.score != null) "quiz" else "lesson"
                )
            }

        SkillsUiState(
            overallReadiness = overallReadiness,
            subjectProgress = subjectProgressList,
            skillCategories = categories,
            recentActivity = recentActivities,
            isLoading = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SkillsUiState()
    )
}
