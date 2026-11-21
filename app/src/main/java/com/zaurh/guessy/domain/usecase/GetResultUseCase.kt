package com.zaurh.guessy.domain.usecase

import com.zaurh.guessy.common.Resource
import com.zaurh.guessy.domain.model.ResultModel
import com.zaurh.guessy.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetResultUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(name: String) : Flow<Resource<ResultModel>>{
        return repository.getName(name)
    }
}