package com.example.agify.domain.repository

import com.example.agify.common.Resource
import com.example.agify.domain.model.ResultModel
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getName(name: String): Flow<Resource<ResultModel>>
}