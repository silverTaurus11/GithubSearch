package com.example.githubsearch.repository

import com.example.githubsearch.model.repositories.SearchResponse
import com.example.githubsearch.model.users.SearchUsersResponse
import io.reactivex.rxjava3.core.Observable

interface GithubRepository {
    fun searchRepository(keyword: String, page: Int, perPage: Int): Observable<SearchResponse>
    fun searchUsers(keyword: String, page: Int, perPage: Int): Observable<SearchUsersResponse>
}