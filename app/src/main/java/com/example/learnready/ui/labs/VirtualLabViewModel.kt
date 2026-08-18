package com.example.learnready.ui.labs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.VirtualLab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

data class LabStep(
    val title: String,
    val instruction: String
)

data class VirtualLabUiState(
    val lab: VirtualLab? = null,
    val steps: List<LabStep> = emptyList(),
    val completedSteps: Set<Int> = emptySet(),
    val currentStepIndex: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class VirtualLabViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LearnReadyRepository
) : ViewModel() {

    private val labId: Int = savedStateHandle.get<Int>("labId") ?: 0

    private val _uiState = MutableStateFlow(VirtualLabUiState())
    val uiState: StateFlow<VirtualLabUiState> = _uiState.asStateFlow()

    init {
        loadLab()
    }

    private fun loadLab() {
        viewModelScope.launch {
            val lab = repository.getLabById(labId)
            if (lab != null) {
                val steps = parseSteps(lab.stepsJson)
                _uiState.value = VirtualLabUiState(
                    lab = lab,
                    steps = steps,
                    isLoading = false
                )
            } else {
                _uiState.value = VirtualLabUiState(isLoading = false)
            }
        }
    }

    fun toggleStepComplete(stepIndex: Int) {
        val current = _uiState.value
        val newCompleted = current.completedSteps.toMutableSet()
        if (stepIndex in newCompleted) {
            newCompleted.remove(stepIndex)
        } else {
            newCompleted.add(stepIndex)
        }
        _uiState.value = current.copy(completedSteps = newCompleted)
    }

    fun goToStep(index: Int) {
        val current = _uiState.value
        if (index in current.steps.indices) {
            _uiState.value = current.copy(currentStepIndex = index)
        }
    }

    private fun parseSteps(json: String): List<LabStep> {
        return try {
            val array = JSONArray(json.trim())
            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                LabStep(
                    title = obj.getString("title"),
                    instruction = obj.getString("instruction")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
