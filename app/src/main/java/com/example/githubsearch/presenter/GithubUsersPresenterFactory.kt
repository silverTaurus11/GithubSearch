package com.example.githubsearch.presenter

import android.content.Context
import com.example.githubsearch.repository.GithubRepositoryFactory
import com.example.githubsearch.view.GithubUsersView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

object GithubUsersPresenterFactory {
    fun createPresenter(context: Context, githubUsersView: GithubUsersView): GithubUsersPresenter{
        val githubRepository = GithubRepositoryFactory.createRepository(context)
        return GithubUsersPresenterImpl(githubRepository, AndroidSchedulers.mainThread(), githubUsersView)
    }
}