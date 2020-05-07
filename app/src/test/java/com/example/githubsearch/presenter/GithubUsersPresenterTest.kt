package com.example.githubsearch.presenter

import com.example.githubsearch.helper.ErrorConst
import com.example.githubsearch.helper.network.NoInternetConnectionException
import com.example.githubsearch.model.users.Owner
import com.example.githubsearch.model.users.SearchUsersResponse
import com.example.githubsearch.repository.GithubRepository
import com.example.githubsearch.repository.GithubRepositoryImpl
import com.example.githubsearch.view.GithubUsersView
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.HttpException
import retrofit2.Response


class GithubUsersPresenterTest {

    private val githubUsersView by lazy {Mockito.mock(GithubUsersView::class.java)}
    private lateinit var githubUsersPresenter: GithubUsersPresenter
    private lateinit var githubRepository: GithubRepository
    private val keyword = "tes"

    @Before
    fun setup(){
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        githubRepository = Mockito.mock(GithubRepositoryImpl::class.java)
        githubUsersPresenter = GithubUsersPresenterImpl(githubRepository,
            Schedulers.trampoline(), githubUsersView)
    }

    @After
    fun after() {
        RxJavaPlugins.reset()
    }

    @Test
    fun requestUsersData_returnSuccessRequest(){
        val searchResponse = dummySearchUsersResponse()
        `when`(githubRepository.searchUsers(eq(keyword), any(), any()))
            .thenReturn(Observable.just(searchResponse))
        githubUsersPresenter.requestUsersData(eq(keyword), any(), any())

        val captor: KArgumentCaptor<List<Owner>> = argumentCaptor()
        verify(githubUsersView).showUsersData(captor.capture(), eq(false))

        Assert.assertEquals(searchResponse.items, captor.lastValue)
    }

    @Test
    fun requestUsersData_returnEmptyUsers(){
        val searchResponse =
            SearchUsersResponse(
                0,
                false,
                mutableListOf()
            )
        `when`(githubRepository.searchUsers(eq(keyword), any(), any()))
            .thenReturn(Observable.just(searchResponse))
        githubUsersPresenter.requestUsersData(eq(keyword), any(), any())
        verify(githubUsersView).emptyUserLayout(eq(false))
    }

    @Test
    fun requestUserData_returnNoInternetConnection(){
        `when`(githubRepository.searchUsers(eq(keyword), any(), any()))
            .thenReturn(Observable.error(NoInternetConnectionException()))

        githubUsersPresenter.requestUsersData(eq(keyword), any(), any())

        val captor: KArgumentCaptor<ErrorConst.GithubError> = argumentCaptor()
        verify(githubUsersView).showErrorToast(captor.capture(), eq(false))

        Assert.assertEquals(ErrorConst.GithubError.NO_INTERNET_CONNECTION, captor.lastValue)
    }

    @Test
    fun requestUserData_returnReachRequestLimit(){
        val reachRequestLimit = HttpException(Response.error<Any>(403,
            "ReachRequestLimit".toResponseBody("".toMediaTypeOrNull())))
        `when`(githubRepository.searchUsers(eq(keyword), any(), any()))
            .thenReturn(Observable.error(reachRequestLimit))

        githubUsersPresenter.requestUsersData(eq(keyword), any(), any())

        verify(githubUsersView).requestLimitLayout(eq(false))
    }

    @Test
    fun requestUserData_returnServerUnavailable(){
        `when`(githubRepository.searchUsers(eq(keyword), any(), any()))
            .thenReturn(Observable.error(Exception()))

        githubUsersPresenter.requestUsersData(eq(keyword), any(), any())

        val captor: KArgumentCaptor<ErrorConst.GithubError> = argumentCaptor()
        verify(githubUsersView).showErrorToast(captor.capture(), eq(false))

        Assert.assertEquals(ErrorConst.GithubError.SERVER_UNAVAILABLE, captor.lastValue)
    }

    @Test
    fun loadMoreData_returnSuccessRequest(){
        val searchResponse = dummySearchUsersResponse()
        `when`(githubRepository.searchUsers(eq(keyword), any(), any()))
            .thenReturn(Observable.just(searchResponse))
        githubUsersPresenter.loadMoreData(eq(keyword), any(), any())

        val captor: KArgumentCaptor<List<Owner>> = argumentCaptor()
        verify(githubUsersView).showUsersData(captor.capture(), eq(true))

        Assert.assertEquals(searchResponse.items, captor.lastValue)
    }

    @Test
    fun loadMoreData_returnEmptyUsers(){
        val searchResponse =
            SearchUsersResponse(
                0,
                false,
                mutableListOf()
            )
        `when`(githubRepository.searchUsers(eq(keyword), any(), any()))
            .thenReturn(Observable.just(searchResponse))
        githubUsersPresenter.loadMoreData(eq(keyword), any(), any())
        verify(githubUsersView).emptyUserLayout(eq(true))
    }

    @Test
    fun loadMoreData_returnNoInternetConnection(){
        `when`(githubRepository.searchUsers(eq(keyword), any(), any()))
            .thenReturn(Observable.error(NoInternetConnectionException()))

        githubUsersPresenter.loadMoreData(eq(keyword), any(), any())

        val captor: KArgumentCaptor<ErrorConst.GithubError> = argumentCaptor()
        verify(githubUsersView).showErrorToast(captor.capture(), eq(true))

        Assert.assertEquals(ErrorConst.GithubError.NO_INTERNET_CONNECTION, captor.lastValue)
    }

    @Test
    fun loadMoreData_returnReachRequestLimit(){
        val reachRequestLimit = HttpException(Response.error<Any>(403,
            "ReachRequestLimit".toResponseBody("".toMediaTypeOrNull())))
        `when`(githubRepository.searchUsers(eq(keyword), any(), any()))
            .thenReturn(Observable.error(reachRequestLimit))

        githubUsersPresenter.loadMoreData(eq(keyword), any(), any())

        verify(githubUsersView).requestLimitLayout(eq(true))
    }

    @Test
    fun loadMoreData_returnServerUnavailable(){
        `when`(githubRepository.searchUsers(eq(keyword), any(), any()))
            .thenReturn(Observable.error(Exception()))

        githubUsersPresenter.loadMoreData(eq(keyword), any(), any())

        val captor: KArgumentCaptor<ErrorConst.GithubError> = argumentCaptor()
        verify(githubUsersView).showErrorToast(captor.capture(), eq(true))

        Assert.assertEquals(ErrorConst.GithubError.SERVER_UNAVAILABLE, captor.lastValue)
    }

    private fun dummySearchUsersResponse(): SearchUsersResponse {
        val owner = Owner("test",
            "123", "www.github.com",
            "https://banner2.cleanpng.com/20180920/yko/kisspng-computer-icons-portable-network" +
                    "-graphics-avatar-ic-5ba3c66df14d32.3051789815374598219884.jpg"
        )
        return SearchUsersResponse(
            1,
            false,
            mutableListOf(owner)
        )
    }

}