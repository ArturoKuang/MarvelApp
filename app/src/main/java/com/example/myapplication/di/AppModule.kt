package com.example.myapplication.di

import com.example.myapplication.Config
import com.example.myapplication.data.remote.MarvelApi
import com.example.myapplication.data.remote.MarvelInterceptor
import com.example.myapplication.data.remote.MarvelRemoteDataSource
import com.example.myapplication.data.remote.MarvelRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        gson: Gson,
        marvelInterceptor: MarvelInterceptor
    ): Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC


        val client = OkHttpClient.Builder()
            .addInterceptor(marvelInterceptor)
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(Config.MARVEL_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
            .setDateFormat(DateFormat.LONG)
            .create()

    @Provides
    fun provideMarvelInterceptor(): MarvelInterceptor =
        MarvelInterceptor()

    @Singleton
    @Provides
    fun provideRepository(
        marvelRemoteDataSource: MarvelRemoteDataSource,
    ): MarvelRepository {

        return MarvelRepository(
            marvelRemoteDataSource,
            Dispatchers.IO
        )
    }

    @Provides
    fun provideMarvelService(retrofit: Retrofit): MarvelApi =
        retrofit.create(MarvelApi::class.java)
}