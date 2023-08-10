package com.example.myapplication.mvi

import com.example.myapplication.fake.MarvelRepositoryFake
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class MarvelListMiddlewareTest {

    private lateinit var middleware: MarvelListMiddleware
    private val marvelRepository: MarvelRepositoryFake = MarvelRepositoryFake()
    private val store: ListStore = mockk()
    private val state: ListViewState = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        middleware = MarvelListMiddleware(marvelRepository)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when list action refresh success`() {
        coEvery { store.dispatch(any()) } returns Unit
        runTest {
            middleware.dispatch(ListAction.RefreshAction, state, store)
            coVerify {
                store.dispatch(ListAction.ListLoadListStarted)
            }
        }

    }
}