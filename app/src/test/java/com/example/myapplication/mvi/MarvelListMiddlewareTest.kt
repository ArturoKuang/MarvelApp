package com.example.myapplication.mvi

import com.example.myapplication.fake.MarvelRepositoryFake
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarvelListMiddlewareTest {

    private lateinit var middleware: MarvelListMiddleware
    private val marvelRepository: MarvelRepositoryFake = MarvelRepositoryFake()
    private val store: ListStore = mockk()
    private val state: ListViewState = mockk()


    @Before
    fun setUp() {
        middleware = MarvelListMiddleware(marvelRepository)
    }

    @Test
    fun `when list action refresh success`() {
        coEvery { store.dispatch(any()) } returns Unit

        runTest {
            middleware.dispatch(ListAction.RefreshAction, state, store)
        }
        coVerify {
            store.dispatch(ListAction.ListLoadListStarted)
        }
    }
}