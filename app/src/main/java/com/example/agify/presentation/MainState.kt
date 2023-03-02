package com.example.agify.presentation

import com.example.agify.domain.model.ResultModel

data class MainState(
    val result: ResultModel? = null,
    val error: String = "",
    val isLoading: Boolean = false
)
