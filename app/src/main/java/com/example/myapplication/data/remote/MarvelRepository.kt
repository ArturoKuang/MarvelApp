package com.example.myapplication.data.remote

import com.example.myapplication.data.local.MarvelEntity
import com.example.myapplication.data.local.MarvelLocalDataSource
import com.example.myapplication.data.local.Preference
import com.example.myapplication.data.model.CharacterResponse
import com.example.myapplication.data.model.Thumbnail
import com.example.myapplication.data.realm.MarvelObject
import com.example.myapplication.data.realm.RealmLocalDataSource
import com.example.myapplication.utils.Resource
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface IMarvelRepository {
    fun getCharacters(): Flow<Resource<CharacterResponse>>
}

class MarvelRepository @Inject constructor(
    private val marvelRemoteDataSource: MarvelRemoteDataSource,
    private val marvelLocalDataSource: MarvelLocalDataSource,
    private val realmLocalDataSource: RealmLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : IMarvelRepository {

    override fun getCharacters(): Flow<Resource<CharacterResponse>> {
        return flow {
            emit(Resource.loading())

            val result = marvelRemoteDataSource.getCharacters()
            result.data?.marvelData?.results?.map {
                MarvelObject(
                    it.name,
                    it.thumbnail.getImagePath(Thumbnail.ImageSize.STANDARD_AMAZING)
                )
            }?.let {
                realmLocalDataSource.addCharacters(it)
            }

            emit(result)
        }.flowOn(dispatcher)
    }

    suspend fun searchCharacters(name: String): List<MarvelEntity> {
        return marvelLocalDataSource.searchCharacters(name)
    }

    fun searchCharacterRealm(name: String): Flow<List<MarvelEntity>> {
        return realmLocalDataSource.searchCharacters(name).map { change ->
            when(change) {
                is InitialResults -> {
                    change.list.map {
                        it.toMarvelEntity()
                    }
                }
                is UpdatedResults -> {
                    change.list.map {
                        it.toMarvelEntity()
                    }
                }
            }
        }
    }

    fun getCharactersRealm(): Flow<List<MarvelEntity>> {
        return realmLocalDataSource.getAllCharacters().map { change ->
            when(change) {
                is InitialResults -> {
                    change.list.map {
                        it.toMarvelEntity()
                    }
                }
                is UpdatedResults -> {
                    change.list.map {
                        it.toMarvelEntity()
                    }
                }
            }
        }
    }

    fun MarvelObject.toMarvelEntity(): MarvelEntity {
        return MarvelEntity(
            name = name,
            thumbnailUrl = thumbnail,
            preference = Preference.NONE
        )
    }
}