package com.example.myapplication.mvi

import com.example.mvi.Middleware
import com.example.mvi.Store

class ListStore(
    initialState: ListViewState,
    reducer: ListReducer,
    middlewares: List<Middleware<ListAction, ListViewState>>,
) : Store<ListAction, ListViewState>(initialState, reducer, middlewares)
