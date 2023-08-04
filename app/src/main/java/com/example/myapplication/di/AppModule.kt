package com.example.myapplication.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.myapplication.Config
import com.example.myapplication.data.local.MarvelDatabase
import com.example.myapplication.data.local.MarvelEntity
import com.example.myapplication.data.local.MarvelRemoteMediator
import com.example.myapplication.data.local.RemoteKeyDatabase
import com.example.myapplication.data.remote.MarvelApi
import com.example.myapplication.data.remote.MarvelInterceptor
import com.example.myapplication.data.remote.MarvelRemoteDataSource
import com.example.myapplication.data.remote.MarvelRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideMarvelDatabase(
        @ApplicationContext appContext: Context
    ): MarvelDatabase {
        return Room.databaseBuilder(
            appContext,
            MarvelDatabase::class.java,
            "marvel_db"
        ).build()
    }

    @Provides
    fun provideMarvelService(retrofit: Retrofit): MarvelApi =
        retrofit.create(MarvelApi::class.java)

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideBeerPager(marvelDb: MarvelDatabase, marvelApi: MarvelRemoteDataSource): Pager<Int, MarvelEntity> {
        return Pager(
            config = PagingConfig(pageSize = 100),
            remoteMediator = MarvelRemoteMediator(
                marvelDatabase = marvelDb,
                marvelService = marvelApi
            ),
            pagingSourceFactory = {
                marvelDb.getMarvelDao().pagingSource()
            }
        )
    }
}