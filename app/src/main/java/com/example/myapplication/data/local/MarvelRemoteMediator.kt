package com.example.myapplication.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.data.model.CharacterResponse
import com.example.myapplication.data.remote.MarvelRemoteDataSource
import com.example.myapplication.ui.marvelPageItemPreview
import com.example.myapplication.utils.Resource
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MarvelRemoteMediator @Inject constructor(
    private val marvelDatabase: MarvelDatabase,
    private val marvelService: MarvelRemoteDataSource
) : RemoteMediator<Int, MarvelEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MarvelEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val nextOffset = (loadKey + 1) * state.config.pageSize
            Timber.d("pages $nextOffset")

            val response =
                marvelService.getCharacters(offset = loadKey * state.config.pageSize, limit = state.config.pageSize)
                    .toMarvelEntityList()

            marvelDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    marvelDatabase.getMarvelDao().clearAll()
                    marvelDatabase.getRemoteKeyDao().clearAll()
                }

                val prevKey = if (loadKey == 0) null else loadKey - 1
                val nextKey = if (response.isEmpty()) null else loadKey + 1

                val keys = response.map {
                    RemoteKeyEntity(name = it.name, prevKey = prevKey, nextKey = nextKey)
                }

                marvelDatabase.getMarvelDao().insertAll(response)
                marvelDatabase.getRemoteKeyDao().insertAll(keys)
            }

            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MarvelEntity>): RemoteKeyEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                marvelDatabase.getRemoteKeyDao().remoteKeysById(character.name)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MarvelEntity>): RemoteKeyEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                // Get the remote keys of the first items retrieved
                marvelDatabase.getRemoteKeyDao().remoteKeysById(character.name)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MarvelEntity>
    ): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.name?.let { name ->
                marvelDatabase.getRemoteKeyDao().remoteKeysById(name)
            }
        }
    }

    companion object {
        var count = 0
        val TAG = MarvelRemoteMediator::class.java.simpleName
    }
}

fun Resource<CharacterResponse>.toMarvelEntityList(): List<MarvelEntity> {
    data ?: return emptyList()
    return data.marvelData.results.map {
        it.toMarvelEntity()
    }
}