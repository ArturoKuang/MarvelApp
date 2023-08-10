package com.example.myapplication.mvi

import com.example.mvi.Action


sealed class ListAction : Action {
    object RefreshAction : ListAction()
    object ListLoadListStarted: ListAction()
    object ListActionListSuccess: ListAction()
    object ListActionListFailed: ListAction()
    object ClickListItemAction : ListAction()
    object Search : ListAction()
}

