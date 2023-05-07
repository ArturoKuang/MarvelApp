package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Thumbnail
import com.example.myapplication.utils.Resource
import com.example.myapplication.viewmodel.MarvelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val marvelViewModel: MarvelViewModel by viewModels()
    private val adapter = MarvelAdapter()
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.rv).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                lifecycleScope.launch {
                    marvelViewModel.characterFlow.collect { response ->
                        if (response.status == Resource.Status.SUCCESS) {
                            val list = response.data!!.data.results.map {
                                MarvelDisplayData(
                                    it.name,
                                    it.thumbnail.getImagePath(Thumbnail.ImageSize.STANDARD_AMAZING)
                                )
                            }

                            adapter.add(list)
                        }
                    }
                }
            }
        }

        findViewById<Button>(R.id.buttonNext).apply {
            setOnClickListener {
                val intent = Intent(this@MainActivity, ComposeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}