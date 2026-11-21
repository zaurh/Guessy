package com.zaurh.guessy.presentation.main

import com.zaurh.guessy.common.Resource
import com.zaurh.guessy.domain.model.ResultModel

/**
 * Pure state transitions for the main screen.
 * The last successful result is kept while loading or on error.
 */
object MainReducer {

    fun onQueryChanged(state: MainState, query: String): MainState =
        state.copy(query = query)

    fun onResult(state: MainState, resource: Resource<ResultModel>): MainState =
        when (resource) {
            is Resource.Loading -> state.copy(isLoading = true, error = "")
            is Resource.Success -> state.copy(result = resource.data, isLoading = false, error = "")
            is Resource.Error -> state.copy(isLoading = false, error = resource.message.orEmpty())
        }
}
