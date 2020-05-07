package com.example.githubsearch.presenter

interface GithubUsersPresenter {
    fun requestUsersData(keyword: String, page: Int, perPage: Int)
    fun loadMoreData(keyword: String, page: Int, perPage: Int)
    fun onStop()
    fun onDestroy()
}