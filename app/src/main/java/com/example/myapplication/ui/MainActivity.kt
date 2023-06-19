package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Thumbnail
import com.example.myapplication.utils.Resource
import com.example.myapplication.viewmodel.MarvelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

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
                marvelViewModel.getChars()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                marvelViewModel.characterStateFlow.collect {
                    adapter.add(list = it)
                }
            }
        }

        findViewById<Button>(R.id.buttonNext).apply {
            setOnClickListener {
                val intent = Intent(this@MainActivity, ComposeActivity::class.java)
                startActivity(intent)
            }
        }

        findViewById<EditText>(R.id.edittext).apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    Timber.d(p0.toString())
                    marvelViewModel.searchCharacters(p0.toString())
                }
            })
        }
    }
}