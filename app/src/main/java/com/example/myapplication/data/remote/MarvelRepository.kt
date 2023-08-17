package com.example.myapplication.data.remote

import com.example.myapplication.data.local.MarvelEntity
import com.example.myapplication.data.local.MarvelLocalDataSource
import com.example.myapplication.data.model.CharacterResponse
import com.example.myapplication.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface IMarvelRepository {
    fun getCharacters(): Flow<Resource<CharacterResponse>>
}

class MarvelRepository @Inject constructor(
    private val marvelRemoteDataSource: MarvelRemoteDataSource,
    private val marvelLocalDataSource: MarvelLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : IMarvelRepository {

    override fun getCharacters(): Flow<Resource<CharacterResponse>> {
        return flow {
            emit(Resource.loading())

            val result = marvelRemoteDataSource.getCharacters()
            emit(result)
        }.flowOn(dispatcher)
    }

    suspend fun searchCharacters(name: String): List<MarvelEntity> {
        return marvelLocalDataSource.searchCharacters(name)
    }
}