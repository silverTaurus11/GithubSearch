package com.example.githubsearch.repository

import android.content.Context
import android.net.ConnectivityManager
import io.reactivex.rxjava3.schedulers.Schedulers

object GithubRepositoryFactory {
    fun createRepository(context: Context): GithubRepository{
        val githubResApi = ConfigurationAPI.create(context)
        return GithubRepositoryImpl(githubResApi, Schedulers.io())
    }
}