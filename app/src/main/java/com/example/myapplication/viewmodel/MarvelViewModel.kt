package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.CharacterResponse
import com.example.myapplication.data.remote.MarvelRepository
import com.example.myapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MarvelViewModel @Inject constructor(
    private val marvelRepository: MarvelRepository
): ViewModel() {

    val characterFlow: Flow<Resource<CharacterResponse>> =
        marvelRepository.getCharacters()
}