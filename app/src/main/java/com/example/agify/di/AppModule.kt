package com.example.agify.di

import com.example.agify.common.Constants.BASE_URL
import com.example.agify.data.remote.AgifyApi
import com.example.agify.data.repository.RepositoryImpl
import com.example.agify.domain.repository.Repository
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
    fun provideRetrofitApi(retrofit: Retrofit): AgifyApi{
        return retrofit.create(AgifyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(api: AgifyApi): Repository = RepositoryImpl(api)

}