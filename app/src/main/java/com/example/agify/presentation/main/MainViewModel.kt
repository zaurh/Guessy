package com.example.agify.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agify.domain.usecase.GetResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getResultUseCase: GetResultUseCase
) : ViewModel() {

    // Backed by Compose snapshot state (not StateFlow) so the TextField query is
    // updated synchronously, avoiding IME/cursor glitches while typing.
    var state by mutableStateOf(MainState())
        private set

    private var searchJob: Job? = null

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.QueryChanged -> search(intent.name)
        }
    }

    private fun search(name: String) {
        state = MainReducer.onQueryChanged(state, name)
        searchJob?.cancel()
        searchJob = getResultUseCase(name)
            .onEach { resource -> state = MainReducer.onResult(state, resource) }
            .launchIn(viewModelScope)
    }
}
