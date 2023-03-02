package com.example.agify.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agify.common.Resource
import com.example.agify.domain.model.ResultModel
import com.example.agify.domain.usecase.GetResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getResultUseCase: GetResultUseCase
): ViewModel(){

    private val _state = mutableStateOf(MainState())
        val state: State<MainState> = _state

    private val _searchQuery = mutableStateOf("")
        val searchQuery: State<String> = _searchQuery

    private var searchJob: Job? = null

    fun onSearch(name: String){
        _searchQuery.value = name
        searchJob?.cancel()
        searchJob = viewModelScope.launch{
//            delay(500L)
            getResultUseCase(name).onEach { result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = MainState(result = result.data)
                    }
                }
            }.launchIn(this)
        }
    }
}