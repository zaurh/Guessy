package com.example.agify.data.remote

import com.example.agify.data.remote.dto.ResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AgifyApi {
    @GET("?")
    suspend fun getName(
        @Query("name") name: String
    ): ResultDto

}