package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.myapplication.data.local.MarvelEntity
import com.example.myapplication.data.local.toMarvelCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class MarvelPagingViewModel @Inject constructor(
    pager: Pager<Int, MarvelEntity>
) : ViewModel() {
    val marvelPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map { it.toMarvelCardData() }
        }.cachedIn(viewModelScope)
}