package com.example.learnready.ui.skills

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.SkillCategory
import com.example.learnready.data.db.entities.SkillModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class SkillModulesUiState(
    val category: SkillCategory? = null,
    val modules: List<SkillModule> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class SkillModulesViewModel @Inject constructor(
    private val repository: LearnReadyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: Int = checkNotNull(savedStateHandle["categoryId"])

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SkillModulesUiState> = flow {
        val category = repository.getSkillCategoryById(categoryId)
        emit(category)
    }.flatMapLatest { category ->
        repository.getSkillModulesForCategory(categoryId).map { modules ->
            SkillModulesUiState(
                category = category,
                modules = modules,
                isLoading = false
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SkillModulesUiState()
    )
}
