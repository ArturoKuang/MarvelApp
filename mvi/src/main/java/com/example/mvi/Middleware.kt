package com.example.mvi

interface Middleware<A: Action, VS: ViewState, S: Store<A, VS>> {
    suspend fun dispatch(action: A, state: VS, store: S)
}