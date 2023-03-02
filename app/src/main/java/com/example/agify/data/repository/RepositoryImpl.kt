package com.example.agify.data.repository

import com.example.agify.common.Resource
import com.example.agify.data.remote.AgifyApi
import com.example.agify.data.remote.dto.toResult
import com.example.agify.domain.model.ResultModel
import com.example.agify.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: AgifyApi
): Repository {

    override fun getName(name: String): Flow<Resource<ResultModel>> = flow{
        try {
            emit(Resource.Loading())
            val remoteData = api.getName(name).toResult()
            emit(Resource.Success(remoteData))
        }
        catch(e: HttpException) {
            emit(Resource.Error(
                message = "Oops, something went wrong!"
            ))
        } catch(e: IOException) {
            emit(Resource.Error(
                message = "Couldn't reach server, check your internet connection"
            ))
        }
    }
}