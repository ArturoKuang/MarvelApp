package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.remote.SSERepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SSEViewModel @Inject constructor(private val sseRepository: SSERepository) : ViewModel() {

    val sseFlow = sseRepository.sseEventsFlow.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        sseRepository.cancelEventSource()
    }
}