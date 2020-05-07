package com.example.githubsearch.presenter

import com.example.githubsearch.helper.ErrorConst
import com.example.githubsearch.helper.ErrorConst.GITHUB_REACH_REQUEST_LIMIT_CODE
import com.example.githubsearch.helper.ObserverAdapter
import com.example.githubsearch.helper.network.NoInternetConnectionException
import com.example.githubsearch.model.repositories.SearchResponse
import com.example.githubsearch.model.users.SearchUsersResponse
import com.example.githubsearch.repository.GithubRepository
import com.example.githubsearch.view.GithubUsersView
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.HttpException

class GithubUsersPresenterImpl(private val githubRepository: GithubRepository,
                               private val scheduler: Scheduler,
                               private val githubUsersView: GithubUsersView): GithubUsersPresenter {

    private var searchRepositoryDisposable: Disposable? = null

    override fun requestUsersData(keyword: String, page: Int, perPage: Int) {
        searchUsers(keyword, page, perPage, false)
    }

    override fun loadMoreData(keyword: String, page: Int, perPage: Int) {
        searchUsers(keyword, page, perPage, true)
    }

    private fun searchUsers(keyword: String, page: Int, perPage: Int, isLoadMore: Boolean){
        githubRepository
            .searchUsers(keyword, page, perPage)
            .observeOn(scheduler)
            .subscribe(object: ObserverAdapter<SearchUsersResponse>() {
                override fun onSubscribe(disposable: Disposable?) {
                    searchRepositoryDisposable = disposable
                }

                override fun onNext(it: SearchUsersResponse) {
                    val itemsSize = it.items.size
                    if(itemsSize > 0){
                        githubUsersView.showUsersData(it.items, isLoadMore)
                    } else{
                        githubUsersView.emptyUserLayout(isLoadMore)
                    }
                }

                override fun onError(e: Throwable?) {
                    if(e is NoInternetConnectionException){
                        showErrorToast(ErrorConst.GithubError.NO_INTERNET_CONNECTION, isLoadMore)
                    } else{
                        if(e is HttpException){
                            if(e.code() == GITHUB_REACH_REQUEST_LIMIT_CODE){
                                githubUsersView.requestLimitLayout(isLoadMore)
                            } else{
                                showErrorToast(ErrorConst.GithubError.SERVER_UNAVAILABLE, isLoadMore)
                            }
                        } else{
                            showErrorToast(ErrorConst.GithubError.SERVER_UNAVAILABLE, isLoadMore)
                        }
                    }
                }
            })
    }

    private fun showErrorToast(error: ErrorConst.GithubError, isLoadMore: Boolean){
        githubUsersView.showErrorToast(error, isLoadMore)
    }

    override fun onStop() {
        searchRepositoryDisposable?.let {
            if(!it.isDisposed){
                it.dispose()
            }
        }
    }

    override fun onDestroy() {
        searchRepositoryDisposable = null
    }
}