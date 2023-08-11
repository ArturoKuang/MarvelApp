package com.example.myapplication.mvi

import com.example.mvi.Action
import com.example.myapplication.data.model.CharacterResponse

/**
 * Using data class here because it is easier to test with
 */
sealed class ListAction : Action {
    object RefreshAction : ListAction()
    object ListLoadListStarted: ListAction()
    data class ListActionListSuccess(val characterResponse: CharacterResponse): ListAction()
    data class ListActionListFailed(val error: String): ListAction()
    object ClickListItemAction : ListAction()
    object Search : ListAction()
}

