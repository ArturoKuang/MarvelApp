package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class FlowViewModel: ViewModel() {
    val flowA = flowOf(1, 2, 3)
    val flowB = flowOf(4, 5, 6)
    val flowC = flowOf(7, 8, 9)
    val flowD = flow<Int> {
        delay(1000L)
        emit(100)
        delay(1000L)
        emit(200)
        delay(1000L)
        emit(300)
        delay(1000L)
        emit(400)
        delay(1000L)
        emit(500)
        delay(1000L)
        emit(600)
        delay(1000L)
        emit(700)
    }

    val collectFlow = combine(flowA, flowB, flowC, flowD) { flowArray ->
        flowArray.toIntArray()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, intArrayOf())


    val map = mutableMapOf<Int, StateFlow<IntArray>>()
    private fun createMap() {
        repeat(1000) { it ->
            map[it] = combine(flowA, flowB, flowC, flowD) { flowArray ->
                flowArray.toIntArray()
            }.stateIn(viewModelScope, SharingStarted.Eagerly, intArrayOf())
        }
    }

    init {
        createMap()
    }


    fun intArrayToInt(arr: Array<Int>): Int {
        return arr.joinToString("").toInt()
    }
}


@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {

    val flowViewModel: FlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            flowViewModel.collectFlow.collect {
                render(it.toString())
            }
        }


        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                ComposeScreen()
//                screen()
            }
        }
    }

    fun render(string: String) {
        Timber.d("render $string")
    }
}