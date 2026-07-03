package com.example.learnready.ui.labs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnready.data.LearnReadyRepository
import com.example.learnready.data.db.entities.VirtualLab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VirtualLabListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: LearnReadyRepository
) : ViewModel() {

    private val subjectId: Int = savedStateHandle.get<Int>("subjectId") ?: 0

    val labs: StateFlow<List<VirtualLab>> = repository.getLabsForSubject(subjectId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
