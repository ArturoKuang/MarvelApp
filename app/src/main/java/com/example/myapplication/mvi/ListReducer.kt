package com.example.myapplication.mvi

import com.example.mvi.Reducer

class ListReducer : Reducer<ListAction, ListViewState> {

    var cacheList: List<ListViewState.ListItemViewState> = listOf()
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
                cacheList = action.characterResponse.asEntity()
                state.copy(
                    list = cacheList,
                    refresh = false
                )
            }

            is ListAction.Search -> {
                state.copy(
                    search = action.query,
                    list = cacheList.filter { it.name.contains(action.query) }
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