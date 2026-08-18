package com.example.learnready.ui.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.Lesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LessonListUiState(
    val moduleName: String = "",
    val lessons: List<Lesson> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class LessonListViewModel @Inject constructor(
    private val repository: LearnReadyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonListUiState())
    val uiState: StateFlow<LessonListUiState> = _uiState.asStateFlow()

    fun loadLessons(moduleId: Int) {
        viewModelScope.launch {
            val module = repository.getModuleById(moduleId)
            _uiState.update { it.copy(moduleName = module?.titleEn ?: "Lessons") }

            repository.getLessonsForModule(moduleId).collect { lessons ->
                _uiState.update {
                    it.copy(lessons = lessons, isLoading = false)
                }
            }
        }
    }
}
