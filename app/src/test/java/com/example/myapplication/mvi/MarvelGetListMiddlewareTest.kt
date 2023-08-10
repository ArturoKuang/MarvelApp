package com.example.myapplication.mvi

import com.example.myapplication.data.remote.MarvelRepository
import com.example.myapplication.fake.MarvelRepositoryFake
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class MarvelGetListMiddlewareTest {

    private val marvelRepository: MarvelRepositoryFake = MarvelRepositoryFake()
    private val store: ListStore = mockk()
    lateinit var middleware: MarvelGetListMiddleware

    @Before
    fun setUp() {
        middleware = MarvelGetListMiddleware(marvelRepository)
    }

    @Test
    fun `when list action refresh success`() {
        middleware.dispatch()

    }

}