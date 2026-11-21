package com.zaurh.guessy.data.repository

import com.zaurh.guessy.common.Resource
import com.zaurh.guessy.data.remote.GuessyApi
import com.zaurh.guessy.data.remote.dto.toResult
import com.zaurh.guessy.domain.model.ResultModel
import com.zaurh.guessy.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: GuessyApi
): Repository {

    override fun getName(name: String): Flow<Resource<ResultModel>> = flow{
        try {
            emit(Resource.Loading())
            val remoteData = api.getName(name).toResult()
            emit(Resource.Success(remoteData))
        }
        catch(e: HttpException) {
            emit(Resource.Error(
                message = if (e.code() == 429) {
                    "Daily guess limit reached.\nPlease try again tomorrow."
                } else {
                    "Oops, something went wrong!"
                }
            ))
        } catch(e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection"
            ))
        }
    }
}