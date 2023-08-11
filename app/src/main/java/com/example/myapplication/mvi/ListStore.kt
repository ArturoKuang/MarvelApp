package com.example.myapplication.mvi

import com.example.mvi.Middleware
import com.example.mvi.Reducer
import com.example.mvi.Store
import com.example.mvi.ViewState

class ListStore(
    initialState: ListViewState,
    reducer: Reducer<ListAction, ListViewState>,
    middlewares: List<Middleware<ListAction, ListViewState, Store<ListAction, ListViewState>>>
) : Store<ListAction, ListViewState>(initialState, reducer, middlewares) {

}