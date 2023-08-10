package com.example.mvi

interface Middleware<A: Action, VS: ViewState, S: Store<A, VS>> {
    fun dispatch(action: A, state: VS, store: S): A
}