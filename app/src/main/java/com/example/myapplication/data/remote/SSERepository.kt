package com.example.myapplication.data.remote

import com.example.myapplication.data.model.SSEEventData
import com.example.myapplication.data.model.STATUS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SSERepository {
    private lateinit var eventSource: EventSource

    private val sseClient = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .build()

    private val sseRequest = Request.Builder()
        .url(EVENTSURL)
        .header("Accept", "application/json")
        .addHeader("Accept", "text/event-stream")
        .build()

    var sseEventsFlow = MutableStateFlow(SSEEventData(STATUS.NONE))
        private set


    private val sseEventSourceListener = object : EventSourceListener() {
        override fun onClosed(eventSource: EventSource) {
            super.onClosed(eventSource)
            Timber.d("onClosed")
            val event = SSEEventData(STATUS.CLOSED)
            sseEventsFlow.tryEmit(event)
        }

        override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
            super.onEvent(eventSource, id, type, data)
            Timber.d("OnEvent() id: $id, type: $type, data: $data")
            if (data.isNotEmpty()) {
                if (data.startsWith("[") && data.endsWith("]")) {
                    val eventList = Json.decodeFromString<List<SSEEventData>>(data)
                    sseEventsFlow.tryEmit(eventList.last())
                } else if (data.startsWith("{") && data.endsWith("}")) {
                    val event = Json.decodeFromString<SSEEventData>(data)
                    sseEventsFlow.tryEmit(event)
                }
            }
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
            super.onFailure(eventSource, t, response)
            Timber.d("onFailure")
            t?.printStackTrace()
            val event = SSEEventData(STATUS.ERROR)
            sseEventsFlow.tryEmit(event)
        }

        override fun onOpen(eventSource: EventSource, response: Response) {
            super.onOpen(eventSource, response)
            Timber.d("onOpen()")
            val event = SSEEventData(STATUS.OPEN)
            sseEventsFlow.tryEmit(event)
        }
    }

    init {
        initEventSource()
    }

    private fun initEventSource() {
        eventSource = EventSources.createFactory(sseClient)
            .newEventSource(request = sseRequest, listener = sseEventSourceListener)
    }

    fun cancelEventSource() {
        eventSource.cancel()
    }


    companion object {
        const val EVENTSURL = "http://192.168.50.152:3000/images"
    }
}