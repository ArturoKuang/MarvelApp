package com.example.myapplication.mvi

import com.example.mvi.Middleware
import com.example.myapplication.data.model.CharacterResponse
import com.example.myapplication.data.remote.IMarvelRepository
import com.example.myapplication.utils.Resource
import timber.log.Timber
import javax.inject.Inject

class MarvelListMiddleware @Inject constructor(
    private val marvelRepository: IMarvelRepository
) : Middleware<ListAction, ListViewState, ListStore> {

    override suspend fun dispatch(action: ListAction, state: ListViewState, store: ListStore) {
        when (action) {
            ListAction.RefreshAction -> {
                getMarvelList(store)
            }

            else -> {
                Timber.d("$action not handled by ${MarvelListMiddleware::class.simpleName}")
            }
        }
    }

    private suspend fun getMarvelList(store: ListStore) {
        marvelRepository.getCharacters().collect { response ->
            when (response.status) {
                Resource.Status.LOADING -> {
                    store.dispatch(ListAction.ListLoadListStarted)
                }

                Resource.Status.SUCCESS -> {
                    handleSuccess(response, store)
                }

                Resource.Status.ERROR -> {
                    handleError(response, store)
                }
            }
        }
    }

    private suspend fun handleError(
        response: Resource<CharacterResponse>, store: ListStore
    ) {
        store.dispatch(
            ListAction.ListActionListFailed(
                response.message ?: "error message is null"
            )
        )
    }

    private suspend fun handleSuccess(
        response: Resource<CharacterResponse>, store: ListStore
    ) {
        if (response.data == null) {
            store.dispatch(
                ListAction.ListActionListFailed(
                    "error: response body is null"
                )
            )
        } else {
            store.dispatch(
                ListAction.ListActionListSuccess(response.data)
            )
        }
    }
}