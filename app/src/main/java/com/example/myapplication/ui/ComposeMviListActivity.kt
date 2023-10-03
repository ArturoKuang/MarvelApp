package com.example.myapplication.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.work.ArrayCreatingInputMerger
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import timber.log.Timber


@AndroidEntryPoint
class ComposeMviListActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

        val download = OneTimeWorkRequestBuilder<DownloadWorker>().apply {
            setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        }.build()


        WorkManager.getInstance(this)
            // Candidates to run in parallel
            .beginWith(listOf(plantName1, plantName2, plantName3))
            // Dependent work (only runs after all previous work in chain)
            .then(cache)
            .then(upload)
            .then(download)
            .enqueue()
    }

    class PlantWorker(context: Context, workerParams: WorkerParameters) :
        Worker(context, workerParams) {
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

    class CacheWorker(context: Context, workerParams: WorkerParameters) :
        Worker(context, workerParams) {
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

    class UploadWorker(context: Context, workerParams: WorkerParameters) :
        Worker(context, workerParams) {
        override fun doWork(): Result {
            val plantName = inputData.getString("resultKey")

            val outputData = Data.Builder()
                .putString("resultKey", "1 $plantName")
                .build()

            Timber.d("$outputData")
            return Result.success(outputData)
        }
    }

    class DownloadWorker(val context: Context, workerParams: WorkerParameters) :
        CoroutineWorker(context, workerParams) {

        private val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager

        @RequiresApi(Build.VERSION_CODES.O)
        override suspend fun doWork(): Result {
            Timber.d("Starting download...")
//            setForeground(createForegroundInfo("Download"))
//            for (i in 0..10000) {
//            }
            return Result.success()
        }

        private fun createForegroundInfo(progress: String): ForegroundInfo {
            val title = "title"
            val cancel = "cancel"
            // This PendingIntent can be used to cancel the worker
            val intent = WorkManager.getInstance(applicationContext)
                .createCancelPendingIntent(id)

            // Create a Notification channel if necessary
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel()
            }

            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(progress)
                .setSmallIcon(com.example.myapplication.R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                // Add the cancel action to the notification which can
                // be used to cancel the worker
                .addAction(android.R.drawable.ic_delete, cancel, intent)
                .build()

            return ForegroundInfo(12, notification)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createChannel() {
            // Create a Notification channel
            val name: CharSequence = "ChannelA"

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = "something"

            notificationManager.createNotificationChannel(channel)
        }


        companion object {
            const val CHANNEL_ID = "123"
        }

    }

}