package com.example.myapplication.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Thumbnail
import com.example.myapplication.data.remote.MarvelRepository
import com.example.myapplication.ui.MarvelStateMvm
import com.example.myapplication.ui.model.MarvelDisplayData
import com.example.myapplication.ui.model.asEntity
import com.example.myapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MarvelViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val marvelRepository: MarvelRepository
) : ViewModel() {

    private val _characterStateFlow: MutableStateFlow<List<MarvelDisplayData>> =
        MutableStateFlow(listOf())

    val characterStateFlow: StateFlow<List<MarvelDisplayData>> = _characterStateFlow

    private val cacheCharacter: MutableList<MarvelDisplayData> = mutableListOf()

    private val currentQuery = savedStateHandle.getLiveData("currentQuery", "")

    private val isLoading = MutableStateFlow(false)

    val marvelList: Flow<List<MarvelDisplayData>> = currentQuery.asFlow()
        .distinctUntilChanged()
        .debounce(TimeUnit.SECONDS.toMillis(1L))
        .onEach { query ->
            if (query.isNotEmpty()) {
                isLoading.value = true
            }
        }
        .flatMapLatest { query ->
            when {
                query.isEmpty() -> emptyFlow()
                else -> flow {
                    val a = marvelRepository.searchCharacters(query).map { it.asEntity() }
                    emit(a)
                }
            }
        }.onEach {
            isLoading.value = false
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())

    val viewState =
        combine(currentQuery.asFlow(), isLoading, _characterStateFlow, marvelList) { query, loading, characters, marvelData ->
            if (query.isEmpty()) {
                MarvelStateMvm(characters, loading, query = query)
            } else {
                MarvelStateMvm(marvelData, loading, query = query)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MarvelStateMvm(listOf(), false))


    fun searchCharacters(name: String) {
        currentQuery.value = name
    }

    fun getChars() {
        viewModelScope.launch {
            marvelRepository.getCharacters().map { response ->
                var list: List<MarvelDisplayData> = emptyList()
                if (response.status == Resource.Status.SUCCESS) {
                    list = response.data?.marvelData?.results?.map {
                        MarvelDisplayData(
                            it.name,
                            it.thumbnail.getImagePath(Thumbnail.ImageSize.STANDARD_AMAZING)
                        )
                    }!!
                }

                list
            }.collect { value ->
                _characterStateFlow.value = value
                cacheCharacter.clear()
                cacheCharacter.addAll(value)
            }
        }
    }

//    fun searchCharacters(name: String) {
//        val matchedCharacterList = cacheCharacter.filter {
//            it.name.lowercase().contains(name.lowercase())
//        }
//
//        _characterStateFlow.value = matchedCharacterList
//    }
}