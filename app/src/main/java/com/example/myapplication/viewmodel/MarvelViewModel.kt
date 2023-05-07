package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.CharacterResponse
import com.example.myapplication.data.model.Thumbnail
import com.example.myapplication.data.remote.MarvelRepository
import com.example.myapplication.ui.MarvelDisplayData
import com.example.myapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarvelViewModel @Inject constructor(
    private val marvelRepository: MarvelRepository
) : ViewModel() {

    val characterFlow: Flow<Resource<CharacterResponse>> =
        marvelRepository.getCharacters()

    private val _characterStateFlow: MutableStateFlow<List<MarvelDisplayData>> =
        MutableStateFlow(listOf())

    val characterStateFlow: StateFlow<List<MarvelDisplayData>> = _characterStateFlow

    fun getChars() {
        viewModelScope.launch {
            marvelRepository.getCharacters().map { response ->
                var list: List<MarvelDisplayData> = emptyList()
                if (response.status == Resource.Status.SUCCESS) {
                    list = response.data?.data?.results?.map {
                        MarvelDisplayData(
                            it.name,
                            it.thumbnail.getImagePath(Thumbnail.ImageSize.STANDARD_AMAZING)
                        )
                    }!!
                }

                list
            }.collect { value ->
                _characterStateFlow.value = value
            }
        }
    }
}