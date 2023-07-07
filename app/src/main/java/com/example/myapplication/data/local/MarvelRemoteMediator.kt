package com.example.myapplication.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.data.model.CharacterResponse
import com.example.myapplication.data.remote.MarvelRemoteDataSource
import com.example.myapplication.utils.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MarvelRemoteMediator @Inject constructor(
    private val database: MarvelDatabase,
    private val marvelService: MarvelRemoteDataSource
) : RemoteMediator<Int, MarvelEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MarvelEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        0
                    } else {
                        state.pages.size + 1
                    }
                }
            }

            val response =
                marvelService.getCharacters(loadKey, state.config.pageSize).toMarvelEntityList()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.marvelDao.clearAll()
                }
                database.marvelDao.insertAll(response)
            }

            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}

fun Resource<CharacterResponse>.toMarvelEntityList(): List<MarvelEntity> {
    return data!!.marvelData.results.map {
        it.toMarvelEntity()
    }
}