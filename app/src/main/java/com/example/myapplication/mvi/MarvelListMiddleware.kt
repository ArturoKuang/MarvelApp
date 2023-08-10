package com.example.myapplication.mvi

import androidx.compose.ui.res.stringArrayResource
import com.example.mvi.Middleware
import com.example.myapplication.data.remote.IMarvelRepository
import timber.log.Timber
import javax.inject.Inject

class MarvelListMiddleware @Inject constructor(
    private val marvelRepository: IMarvelRepository
) : Middleware<ListAction, ListViewState, ListStore> {

    override suspend fun dispatch(action: ListAction, state: ListViewState, store: ListStore) {
        when (action) {
            ListAction.RefreshAction -> {
                getMarvelList(action, state, store)
            }
            else -> {
                Timber.d("$action not handled by ${MarvelListMiddleware::class.simpleName}")
            }
        }
    }

    private suspend fun getMarvelList(action: ListAction, state: ListViewState, store: ListStore) {
        store.dispatch(ListAction.ListLoadListStarted)
    }
}