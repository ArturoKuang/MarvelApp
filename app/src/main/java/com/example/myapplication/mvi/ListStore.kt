package com.example.myapplication.mvi

import com.example.mvi.Store
import com.example.mvi.ViewState

class ListStore : Store<ListAction, ListViewState>(
    initialState = ListViewState(),
    reducer = ListReducer(),
    middlewares = listOf(LoggingMiddleware())
) {

}