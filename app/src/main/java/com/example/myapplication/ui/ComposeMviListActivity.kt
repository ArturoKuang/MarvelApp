package com.example.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.work.ArrayCreatingInputMerger
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import timber.log.Timber

@AndroidEntryPoint
class ComposeMviListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                ListScreen()
            }
        }


        val plantName1 = OneTimeWorkRequestBuilder<PlantWorker>()
            .setInputData(Data.Builder().putString("plantName", "Rose").build())
            .build()

        val plantName2 = OneTimeWorkRequestBuilder<PlantWorker>()
            .setInputData(Data.Builder().putString("plantName", "Tulip").build())
            .build()

        val plantName3 = OneTimeWorkRequestBuilder<PlantWorker>()
            .setInputData(Data.Builder().putString("plantName", "Daisy").build())
            .build()

        val cache = OneTimeWorkRequestBuilder<CacheWorker>()
            .setInputMerger(ArrayCreatingInputMerger::class.java)
            .build()

        val upload = OneTimeWorkRequestBuilder<UploadWorker>().build()


        WorkManager.getInstance(this)
            // Candidates to run in parallel
            .beginWith(listOf(plantName1, plantName2, plantName3))
            // Dependent work (only runs after all previous work in chain)
            .then(cache)
            .then(upload)
            // Call enqueue to kick things off
            .enqueue()
    }

    class PlantWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
        override fun doWork(): Result {
            val plantName = inputData.getString("plantName")

//            increment()
            val outputData = Data.Builder()
                .putString("resultKey${count}", "Result for $plantName")
                .build()

            Timber.d("$outputData")
            return Result.success(outputData)
        }

        companion object {
            var count = 0

            @OptIn(InternalCoroutinesApi::class)
            fun increment() {
                synchronized(count) {
                    count++
                }
            }
        }
    }

    class CacheWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
        override fun doWork(): Result {
            val plantName = inputData.getStringArray("resultKey0")

            Timber.d(plantName?.joinToString())

            val outputData = Data.Builder()
                .putString("resultKey", "0 $plantName")
                .build()

            Timber.d("$outputData")
            return Result.success(outputData)
        }
    }

    class UploadWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
        override fun doWork(): Result {
            val plantName = inputData.getString("resultKey")

            val outputData = Data.Builder()
                .putString("resultKey", "1 $plantName")
                .build()

            Timber.d("$outputData")
            return Result.success(outputData)
        }
    }

}