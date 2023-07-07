package com.example.myapplication.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.myapplication.ui.model.MarvelCardData
import androidx.paging.compose.collectAsLazyPagingItems


@Composable
fun PagingScreen(
    marvelCharacters: LazyPagingItems<MarvelCardData>
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = marvelCharacters.loadState) {
        if (marvelCharacters.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (marvelCharacters.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (marvelCharacters.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(count = marvelCharacters.itemCount) { index ->
                    val marvelCharacter = marvelCharacters[index]
                    if (marvelCharacter != null) {
                        MarvelPagingItem(
                            marvelCardData = marvelCharacter, modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                item {
                    if (marvelCharacters.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}