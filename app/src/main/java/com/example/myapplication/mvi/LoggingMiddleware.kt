package com.example.myapplication.mvi

import com.example.mvi.Action
import com.example.mvi.Middleware
import com.example.mvi.Store
import com.example.mvi.ViewState
import timber.log.Timber

class LoggingMiddleware<A: Action, VS: ViewState>: Middleware<A, VS> {
    override suspend fun dispatch(action: A, state: VS, store: Store<A, VS>) {
        Timber.d("action $action, state: $state, store: $store")
    }
}