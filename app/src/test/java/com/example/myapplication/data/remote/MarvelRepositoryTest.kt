package com.example.myapplication.data.remote

import com.example.myapplication.utils.Resource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import javax.inject.Inject


@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class MarvelRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var marvelRepository: MarvelRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getCharacters should emit loading and success status`() {
        runTest {
            val resultFlow = marvelRepository.getCharacters()

            // Then
            val resultList = resultFlow.take(2).toList()

            assertTrue(resultList[0].status == Resource.Status.LOADING)

            if (resultList[1].status == Resource.Status.ERROR) {
                println("ERROR: " + resultList[1].message)
            }

            assertTrue(resultList[1].status == Resource.Status.SUCCESS)
        }
    }
}
