package com.zaurh.guessy.domain.repository

import com.zaurh.guessy.common.Resource
import com.zaurh.guessy.domain.model.ResultModel
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getName(name: String): Flow<Resource<ResultModel>>
}