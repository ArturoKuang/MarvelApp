package com.example.mvi

interface Middleware<A: Action, VS: ViewState> {
    suspend fun dispatch(action: A, state: VS, store: Store<A, VS>)
}