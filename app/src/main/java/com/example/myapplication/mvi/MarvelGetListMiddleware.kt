package com.example.myapplication.mvi

import com.example.mvi.Action
import com.example.mvi.Middleware
import com.example.mvi.ViewState
import com.example.myapplication.data.remote.IMarvelRepository
import com.example.myapplication.data.remote.MarvelRepository
import javax.inject.Inject

class MarvelGetListMiddleware @Inject constructor(
    private val marvelRepository: IMarvelRepository
) : Middleware<ListAction, ListViewState, ListStore> {

    override fun dispatch(action: ListAction, state: ListViewState, store: ListStore): ListAction {

    }
}