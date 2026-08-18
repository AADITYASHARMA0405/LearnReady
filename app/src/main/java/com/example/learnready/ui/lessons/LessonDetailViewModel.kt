package com.example.learnready.ui.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LessonDetailUiState(
    val lessonTitle: String = "",
    val lessonTitleKn: String = "",
    val contentEn: String = "",
    val contentKn: String = "",
    val keyPointsEn: String = "",
    val keyPointsKn: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val repository: LearnReadyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonDetailUiState())
    val uiState: StateFlow<LessonDetailUiState> = _uiState.asStateFlow()

    fun loadLesson(lessonId: Int) {
        viewModelScope.launch {
            val lesson = repository.getLessonById(lessonId)
            if (lesson != null) {
                _uiState.update {
                    LessonDetailUiState(
                        lessonTitle = lesson.titleEn,
                        lessonTitleKn = lesson.titleKn,
                        contentEn = lesson.contentEn,
                        contentKn = lesson.contentKn,
                        keyPointsEn = lesson.keyPointsEn,
                        keyPointsKn = lesson.keyPointsKn,
                        isLoading = false
                    )
                }
            }
        }
    }
}
