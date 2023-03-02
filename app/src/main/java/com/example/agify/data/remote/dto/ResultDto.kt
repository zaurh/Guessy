package com.example.agify.data.remote.dto

import com.example.agify.domain.model.ResultModel

data class ResultDto(
    val count: Int,
    val gender: String,
    val name: String,
    val probability: Double
)

fun ResultDto.toResult() = ResultModel(count, gender, name, probability)