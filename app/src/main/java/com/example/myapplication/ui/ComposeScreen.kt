package com.example.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.ui.model.MarvelDisplayData
import com.example.myapplication.viewmodel.MarvelViewModel

@Composable
fun ComposeScreen(
    modifier: Modifier = Modifier,
    marvelViewModel: MarvelViewModel = viewModel()
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }

    val characterList =
        marvelViewModel.characterStateFlow.collectAsState()


    Column {
        TextButton(onClick = { marvelViewModel.getChars() }) {
            Text(text = "Load List")
        }

        TextField(
            value = textState.value,
            onValueChange = { newText: TextFieldValue ->
                textState.value = newText
                marvelViewModel.searchCharacters(newText.text)
            },
            label = { Text("Enter something") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

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