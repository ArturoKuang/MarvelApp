package com.example.mvi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class Store<A: Action, S: ViewState>(
    private val initialState: S,
    private val reducer: Reducer<A, S>,
    private val middlewares: List<Middleware<A, S>>
) {
    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

    suspend fun dispatch(action: A) {
        middlewares.forEach { middleware ->
            middleware.dispatch(action, state.value, this)
        }

        _state.value = reducer.reduce(action, state.value)
    }
}