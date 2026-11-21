package com.zaurh.guessy.data.remote.dto

import com.zaurh.guessy.domain.model.ResultModel

data class ResultDto(
    val count: Int,
    val gender: String?, // null when the name is unknown
    val name: String,
    val probability: Double
)

fun ResultDto.toResult() = ResultModel(count, gender, name, probability)