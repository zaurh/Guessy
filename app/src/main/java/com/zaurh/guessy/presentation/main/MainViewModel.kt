package com.zaurh.guessy.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.guessy.domain.usecase.GetResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        if (name.isBlank()) {
            state = state.copy(result = null, isLoading = false, error = "")
            return
        }
        searchJob = viewModelScope.launch {
            // Debounce so we don't hit the API (and its rate limit) on every keystroke.
            delay(SEARCH_DEBOUNCE_MS)
            getResultUseCase(name.trim())
                .collect { resource -> state = MainReducer.onResult(state, resource) }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 400L
    }
}
