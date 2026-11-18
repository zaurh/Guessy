package com.example.agify.presentation.main

import com.example.agify.domain.model.ResultModel

/**
 * MVI contract for the main screen.
 */

/** Single immutable source of truth rendered by the UI. */
data class MainState(
    val query: String = "",
    val result: ResultModel? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)

/** User actions coming from the UI. */
sealed interface MainIntent {
    data class QueryChanged(val name: String) : MainIntent
}
