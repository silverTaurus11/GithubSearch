package com.example.githubsearch.repository

import com.example.githubsearch.model.SearchResponse
import io.reactivex.rxjava3.core.Observable

interface GithubRepository {
    fun searchRepository(keyword: String, page: Int, perPage: Int): Observable<SearchResponse>
}