package com.example.myapplication.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
@Preview
fun MainScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Button(onClick = {
            val intent = Intent(context, XmlActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "XML List")
        }

        Button(onClick = {
            val intent = Intent(context, ComposeActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Compose List")
        }

        Button(onClick = {
            val intent = Intent(context, ComposePagingActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Compose Paging")
        }

        Button(onClick = {
            val intent = Intent(context, ComposePagingActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Compose Card Swipe")
        }

        Button(onClick = {
            val intent = Intent(context, ComposeMviListActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "MVI Compose List")
        }

        Button(onClick = {
            val intent = Intent(context, SSEActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "SSE")
        }
    }
}