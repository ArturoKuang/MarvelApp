package com.example.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.viewmodel.MarvelViewModel

@Composable
fun ComposeScreen(
    modifier: Modifier = Modifier,
    marvelViewModel: MarvelViewModel = viewModel()
) {
    val characterList =
        marvelViewModel.characterStateFlow.collectAsState()


    Column {
        TextButton(onClick = { marvelViewModel.getChars() }) {
            Text(text = "Load List")
        }
        CharacterList(list = characterList.value)
    }
}


@Composable
fun CharacterList(modifier: Modifier = Modifier, list: List<MarvelDisplayData>) {
    LazyColumn(modifier) {
        items(items = list, key = {
            it.name
        }) {
            Column {
                Text(text = it.name, style = TextStyle(fontSize = 20.sp))
                AsyncImage(
                    model = it.imageUrl,
                    contentDescription = null
                )
            }
        }
    }
}