package com.example.learnready.ui.skills

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.SkillModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class SkillLessonUiState(
    val module: SkillModule? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class SkillLessonViewModel @Inject constructor(
    private val repository: LearnReadyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val moduleId: Int = checkNotNull(savedStateHandle["moduleId"])

    val uiState: StateFlow<SkillLessonUiState> = flow {
        val module = repository.getSkillModuleById(moduleId)
        emit(
            SkillLessonUiState(
                module = module,
                isLoading = false
            )
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SkillLessonUiState()
    )
}
