package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.remote.MarvelRepository
import com.example.myapplication.mvi.ListAction
import com.example.myapplication.mvi.ListReducer
import com.example.myapplication.mvi.ListStore
import com.example.myapplication.mvi.ListViewState
import com.example.myapplication.mvi.LoggingMiddleware
import com.example.myapplication.mvi.MarvelListMiddleware
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarvelMviViewModel @Inject constructor(marvelRepository: MarvelRepository) : ViewModel() {

    private val store = ListStore(
        initialState = ListViewState(), reducer = ListReducer(), middlewares = listOf(
            MarvelListMiddleware(marvelRepository = marvelRepository), LoggingMiddleware()
        )
    )

    val viewState: StateFlow<ListViewState> = store.state

    fun search(query: String) {
        viewModelScope.launch {
            store.dispatch(ListAction.Search(query))
        }
    }

    fun refresh() {
        viewModelScope.launch {
            store.dispatch(ListAction.RefreshAction)
        }
    }

    fun clickItem(listItemViewState: ListViewState.ListItemViewState) {
        viewModelScope.launch {
            store.dispatch(ListAction.ClickListItemAction(listItemViewState))
        }
    }
}