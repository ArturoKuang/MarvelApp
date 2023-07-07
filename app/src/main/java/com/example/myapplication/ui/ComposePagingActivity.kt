package com.example.myapplication.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myapplication.viewmodel.MarvelPagingViewModel

class ComposePagingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                val viewModel = hiltViewModel<MarvelPagingViewModel>()
                val marvelCharacters = viewModel.marvelPagingFlow.collectAsLazyPagingItems()
                PagingScreen(marvelCharacters = marvelCharacters)
            }
        }
    }
}