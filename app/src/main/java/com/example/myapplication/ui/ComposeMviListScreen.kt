package com.example.myapplication.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.mvi.ListViewState
import com.example.myapplication.viewmodel.MarvelMviViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListScreen(
    modifier: Modifier = Modifier, marvelMviViewModel: MarvelMviViewModel = viewModel()
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val viewState = marvelMviViewModel.viewState.collectAsState()
    val pullRefreshState =
        rememberPullRefreshState(viewState.value.refresh, { marvelMviViewModel.refresh() })
    val context = LocalContext.current
    if (viewState.value.popUpMessage.isNotEmpty())
        Toast.makeText(context, viewState.value.popUpMessage, Toast.LENGTH_SHORT).show()

    RuntimePermissionsDialog(
        Manifest.permission.POST_NOTIFICATIONS,
        onPermissionDenied = {},
        onPermissionGranted = {},
    )

    Column(
        Modifier.pullRefresh(pullRefreshState)
    ) {
        PullRefreshIndicator(viewState.value.refresh, pullRefreshState)
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
                    model = characterViewState.imageUrl, contentDescription = null
                )
            }
        }
    }
}


@Composable
fun RuntimePermissionsDialog(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        if (ContextCompat.checkSelfPermission(
                LocalContext.current,
                permission) != PackageManager.PERMISSION_GRANTED) {

            val requestLocationPermissionLauncher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->

                    if (isGranted) {
                        onPermissionGranted()
                    } else {
                        onPermissionDenied()
                    }
                }

            SideEffect {
                requestLocationPermissionLauncher.launch(permission)
            }
        }
    }
}
