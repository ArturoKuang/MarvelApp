package com.example.myapplication.mvi

import com.example.myapplication.fake.MarvelRepositoryFake
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarvelListMiddlewareTest {

    private lateinit var middleware: MarvelListMiddleware
    private val marvelRepository: MarvelRepositoryFake = MarvelRepositoryFake()
    private val store: ListStore = mockk(relaxed = true)
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
        //Given
        marvelRepository.behavior = MarvelRepositoryFake.Behavior.SUCCESS
        runTest {
            //When
            middleware.dispatch(ListAction.RefreshAction, state, store)
            //Then
            assertAction(ListAction.ListActionListSuccess(marvelRepository.characterResponse))
        }
    }

    @Test
    fun `when list action refresh fail`() {
        //Given
        marvelRepository.behavior = MarvelRepositoryFake.Behavior.ERROR
        runTest {
            //When
            middleware.dispatch(ListAction.RefreshAction, state, store)
            //Then
            assertAction(ListAction.ListActionListFailed(marvelRepository.errorMessage))
        }
    }

    private fun assertAction(action: ListAction) {
        val capturedActions = mutableListOf<ListAction>()
        coVerify {
            store.dispatch(capture(capturedActions))
        }

        Assert.assertEquals(2, capturedActions.size)
        Assert.assertEquals(ListAction.ListLoadListStarted, capturedActions[0])
        Assert.assertEquals(
            action, capturedActions[1]
        )
    }
}