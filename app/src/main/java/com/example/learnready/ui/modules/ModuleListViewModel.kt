package com.example.learnready.ui.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.Module
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ModuleWithLessons(
    val module: Module,
    val lessonCount: Int
)

data class ModuleListUiState(
    val subjectName: String = "",
    val modules: List<ModuleWithLessons> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ModuleListViewModel @Inject constructor(
    private val repository: LearnReadyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModuleListUiState())
    val uiState: StateFlow<ModuleListUiState> = _uiState.asStateFlow()

    fun loadModules(subjectId: Int) {
        viewModelScope.launch {
            val subject = repository.getSubjectById(subjectId)
            _uiState.update { it.copy(subjectName = subject?.nameEn ?: "Subject") }

            repository.getModulesForSubject(subjectId).collect { modules ->
                val modulesWithLessons = modules.map { module ->
                    val lessonCount = repository.getLessonCountForModule(module.id)
                    ModuleWithLessons(module, lessonCount)
                }
                _uiState.update {
                    it.copy(modules = modulesWithLessons, isLoading = false)
                }
            }
        }
    }
}
