package com.example.mvi

interface Reducer<A: Action, S: ViewState> {
    fun reduce(action: A, state: S): S
}