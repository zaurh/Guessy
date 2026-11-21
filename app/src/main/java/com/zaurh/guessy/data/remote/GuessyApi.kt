package com.zaurh.guessy.data.remote

import com.zaurh.guessy.data.remote.dto.ResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GuessyApi {
    @GET("?")
    suspend fun getName(
        @Query("name") name: String
    ): ResultDto

}