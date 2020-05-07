package com.example.githubsearch.repository

import com.example.githubsearch.model.SearchResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler

class GithubRepositoryImpl(private val githubResApi: GithubResApi,
                           private val scheduler: Scheduler): GithubRepository {
    override fun searchRepository(
        keyword: String,
        page: Int,
        perPage: Int
    ): Observable<SearchResponse> {
        return githubResApi.getRepositoryData(keyword, page, perPage).subscribeOn(scheduler)
    }
}