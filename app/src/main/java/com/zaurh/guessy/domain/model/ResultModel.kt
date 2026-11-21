package com.zaurh.guessy.domain.model

data class ResultModel(
    val count: Int,
    val gender: String?,
    val name: String,
    val probability: Double
)
