package com.example.myapplication.ui

import androidx.compose.foundation.clickable
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
import com.example.myapplication.mvi.ListViewState
import com.example.myapplication.viewmodel.MarvelMviViewModel

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    marvelMviViewModel: MarvelMviViewModel = viewModel()
) {
    // Perhaps we can store in viewmodel, flow, and add debounce logic
    // https://proandroiddev.com/reclaim-the-reactivity-of-your-state-management-say-no-to-imperative-mvi-3b23ca6b8537
    val textState = remember { mutableStateOf(TextFieldValue()) }

    val viewState = marvelMviViewModel.viewState.collectAsState()

    Column {
        TextButton(onClick = { marvelMviViewModel.refresh() }) {
            Text(text = "Load List")
        }

        TextField(
            value = textState.value,
            onValueChange = { newText: TextFieldValue ->
                textState.value = newText
                marvelMviViewModel.search(newText.text)
            },
            label = { Text("Enter something") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        CharacterList(list = viewState.value.list, marvelMviViewModel = marvelMviViewModel)
    }
}

@Composable
fun CharacterList(
    modifier: Modifier = Modifier,
    list: List<ListViewState.ListItemViewState>,
    marvelMviViewModel: MarvelMviViewModel
) {
    LazyColumn(modifier.fillMaxWidth()) {
        items(items = list, key = { item ->
            item.name
        }) { characterViewState ->
            Column(modifier = Modifier.clickable {
                marvelMviViewModel.clickItem(characterViewState)
            }) {
                Text(text = characterViewState.name, style = TextStyle(fontSize = 20.sp))
                AsyncImage(
                    model = characterViewState.imageUrl,
                    contentDescription = null
                )
            }
        }
    }
}