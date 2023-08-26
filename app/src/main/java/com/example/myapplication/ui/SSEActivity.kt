package com.example.myapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.myapplication.viewmodel.SSEViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SSEActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: SSEViewModel = hiltViewModel()
            val event = viewModel.sseFlow.collectAsStateWithLifecycle()
            SSEMainScreen(imageUrl = event.value.image)
        }
    }
}

@Composable
fun SSEMainScreen(modifier: Modifier = Modifier, imageUrl: String?) {
    if (imageUrl == null) {
        Timber.d("not valid image")
    }
    AsyncImage(model = imageUrl, contentDescription = null)
}