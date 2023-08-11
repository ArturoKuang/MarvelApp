package com.example.myapplication.mvi

import com.example.mvi.Reducer

class ListReducer : Reducer<ListAction, ListViewState> {
    override fun reduce(action: ListAction, state: ListViewState): ListViewState {
        return when (action) {
            is ListAction.ClickListItemAction -> {
                state.copy(
                    popUpMessage = "item clicked ${action.item}"
                )
            }
            is ListAction.ListActionListFailed -> {
                state.copy(
                    popUpMessage = action.error,
                    refresh = false
                )
            }
            is ListAction.ListActionListSuccess -> {
                state.copy(
                    list = action.characterResponse.asEntity(),
                    refresh = false
                )
            }
            is ListAction.Search -> {
                state.copy(
                    search = action.query,
                    list = state.list.filter { it.name.contains(action.query) }
                )
            }
            ListAction.ListLoadListStarted -> {
                state.copy(
                    refresh = true
                )
            }
            ListAction.RefreshAction -> {
                state
            }
        }
    }
}