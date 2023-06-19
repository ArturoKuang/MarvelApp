package com.example.myapplication.viewmodel

import androidx.compose.ui.text.toLowerCase
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

    private val _characterStateFlow: MutableStateFlow<List<MarvelDisplayData>> =
        MutableStateFlow(listOf())

    val characterStateFlow: StateFlow<List<MarvelDisplayData>> = _characterStateFlow

    private val cacheCharacter: MutableList<MarvelDisplayData> = mutableListOf()

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
                cacheCharacter.clear()
                cacheCharacter.addAll(value)
            }
        }
    }

    fun searchCharacters(name: String) {
        val matchedCharacterList = cacheCharacter.filter {
            it.name.lowercase().contains(name.lowercase())
        }

         _characterStateFlow.value = matchedCharacterList
    }
}