package com.zaurh.guessy.di

import com.zaurh.guessy.common.Constants.BASE_URL
import com.zaurh.guessy.data.remote.GuessyApi
import com.zaurh.guessy.data.repository.RepositoryImpl
import com.zaurh.guessy.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitApi(retrofit: Retrofit): GuessyApi{
        return retrofit.create(GuessyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(api: GuessyApi): Repository = RepositoryImpl(api)

}