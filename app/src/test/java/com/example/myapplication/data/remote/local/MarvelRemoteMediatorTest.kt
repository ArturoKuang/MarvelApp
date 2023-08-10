package com.example.myapplication.data.remote.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.myapplication.data.local.MarvelDatabase
import com.example.myapplication.data.local.MarvelEntity
import com.example.myapplication.data.local.MarvelRemoteMediator
import com.example.myapplication.data.remote.MarvelRemoteDataSource
import com.example.myapplication.data.remote.TestUtils
import io.mockk.coEvery
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
class MarvelRemoteMediatorTest {
    lateinit var marvelDatabase: MarvelDatabase


    @Before
    fun init() {
        marvelDatabase = TestModule.marvelDatabase
    }

    private var marvelRemoteDataSource: MarvelRemoteDataSource = TestModule.marvelRemoteDataSource

    @Test
    fun `test load refresh`() = runTest {
        val mockResponse = TestUtils.getMockResponse("marvel_response.json")
        coEvery { TestModule.mockMarvelApi.getCharacters() } returns Response.success(mockResponse)

        // Arrange
        val mediator = MarvelRemoteMediator(marvelDatabase, marvelRemoteDataSource)
        val pagingState = PagingState<Int, MarvelEntity>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )

        // Act
        val result = mediator.load(LoadType.REFRESH, pagingState)
        // Assert
        assertTrue(result is RemoteMediator.MediatorResult.Success)

        // Assert
        val dbResult = TestModule.marvelDatabase.getMarvelDao().pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        println(dbResult)
    }
}


