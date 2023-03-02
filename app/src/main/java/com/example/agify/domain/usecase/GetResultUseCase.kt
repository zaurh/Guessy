package com.example.agify.domain.usecase

import com.example.agify.common.Resource
import com.example.agify.domain.model.ResultModel
import com.example.agify.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetResultUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(name: String) : Flow<Resource<ResultModel>>{
        return repository.getName(name)
    }
}