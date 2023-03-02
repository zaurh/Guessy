package com.example.agify.domain.model

data class ResultModel(
    val count: Int,
    val gender: String?,
    val name: String,
    val probability: Double
)
