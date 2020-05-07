package com.example.githubsearch.repository

import com.example.githubsearch.model.SearchResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubResApi {
    @Headers("Content-Type: application/json")
    @GET("/search/repositories")
    fun getRepositoryData(
        @Query("q") query:String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int): Observable<SearchResponse>
}