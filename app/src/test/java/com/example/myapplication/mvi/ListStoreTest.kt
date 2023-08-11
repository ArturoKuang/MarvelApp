package com.example.myapplication.mvi

import app.cash.turbine.test
import com.example.mvi.Middleware
import com.example.myapplication.fake.MarvelRepositoryFake
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListStoreTest {
    private val fakeMarvelRepository = MarvelRepositoryFake()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadListSuccess() {
        //given
        val store = createListStore(ListViewState())
        fakeMarvelRepository.behavior = MarvelRepositoryFake.Behavior.SUCCESS
        runTest {
            store.state.test {
                //when
                store.dispatch(ListAction.RefreshAction)
                //then
                assertThat(awaitItem()).isEqualTo(ListViewState())
                assertThat(awaitItem()).isEqualTo(ListViewState(refresh = true))
                assertThat(awaitItem()).isEqualTo(
                    ListViewState(
                        refresh = false, list = fakeMarvelRepository.characterResponse.asEntity()
                    )
                )
                assertThat(0 as Int).isEqualTo(cancelAndConsumeRemainingEvents().size)
            }
        }
    }

    @Test
    fun testLoadListFail() {
        val store = createListStore(ListViewState())
        fakeMarvelRepository.behavior = MarvelRepositoryFake.Behavior.ERROR
        runTest {
            store.state.test {
                //when
                store.dispatch(ListAction.RefreshAction)
                //then
                assertThat(awaitItem()).isEqualTo(ListViewState())
                assertThat(awaitItem()).isEqualTo(ListViewState(refresh = true))
                assertThat(awaitItem()).isEqualTo(
                    ListViewState(
                        refresh = false, popUpMessage = fakeMarvelRepository.errorMessage
                    )
                )
                assertThat(0 as Int).isEqualTo(cancelAndConsumeRemainingEvents().size)
            }
        }
    }

    @Test
    fun testItemClicked() {
        val store = createListStore(ListViewState())
        fakeMarvelRepository.behavior = MarvelRepositoryFake.Behavior.SUCCESS
        val characterList = fakeMarvelRepository.characterResponse.asEntity()
        val characterClicked = characterList.first()
        runTest {
            store.state.test {
                //when
                store.dispatch(ListAction.RefreshAction)
                //then
                assertThat(awaitItem()).isEqualTo(ListViewState())
                assertThat(awaitItem()).isEqualTo(ListViewState(refresh = true))
                assertThat(awaitItem()).isEqualTo(
                    ListViewState(
                        refresh = false, list = fakeMarvelRepository.characterResponse.asEntity()
                    )
                )
                store.dispatch(ListAction.ClickListItemAction(characterClicked))
                assertThat(awaitItem()).isEqualTo(
                    ListViewState(
                        refresh = false,
                        list = fakeMarvelRepository.characterResponse.asEntity(),
                        popUpMessage = "item clicked $characterClicked"
                    )
                )
                assertThat(0 as Int).isEqualTo(cancelAndConsumeRemainingEvents().size)
            }
        }
    }


    @Test
    fun testSearchWithEmptyList() {
        val store = createListStore(ListViewState())
        fakeMarvelRepository.behavior = MarvelRepositoryFake.Behavior.SUCCESS
        runTest {
            store.state.test {
                //when
                store.dispatch(ListAction.Search("test"))
                //then
                assertThat(awaitItem()).isEqualTo(ListViewState())
                assertThat(awaitItem()).isEqualTo(
                    ListViewState(
                        search = "test", list = emptyList()
                    )
                )
                assertThat(0 as Int).isEqualTo(cancelAndConsumeRemainingEvents().size)
            }
        }
    }

    @Test
    fun testSearchWithNonEmptyList() {
        //given
        val store = createListStore(ListViewState())
        fakeMarvelRepository.behavior = MarvelRepositoryFake.Behavior.SUCCESS
        val searchQuery = "a"
        val queryList = fakeMarvelRepository.characterResponse.asEntity()
            .filter { it.name.contains(searchQuery) }

        runTest {
            store.state.test {
                //when
                store.dispatch(ListAction.RefreshAction)
                //then
                assertThat(awaitItem()).isEqualTo(ListViewState())
                assertThat(awaitItem()).isEqualTo(ListViewState(refresh = true))
                assertThat(awaitItem()).isEqualTo(
                    ListViewState(
                        refresh = false, list = fakeMarvelRepository.characterResponse.asEntity()
                    )
                )
                store.dispatch(ListAction.Search(searchQuery))
                assertThat(awaitItem()).isEqualTo(
                    ListViewState(
                        search = searchQuery,
                        refresh = false,
                        list = queryList,
                    )
                )

                assertThat(0 as Int).isEqualTo(cancelAndConsumeRemainingEvents().size)
            }
        }
    }

    private fun createListStore(initialState: ListViewState): ListStore {
        val middlewares: List<Middleware<ListAction, ListViewState>> =
            listOf(MarvelListMiddleware(fakeMarvelRepository), LoggingMiddleware())

        return ListStore(
            initialState = initialState, reducer = ListReducer(), middlewares = middlewares
        )
    }
}