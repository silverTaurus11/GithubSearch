package com.example.githubsearch.view

import com.example.githubsearch.helper.ErrorConst
import com.example.githubsearch.model.users.Owner

interface GithubUsersView {
    fun showUsersData(itemList: List<Owner>, isLoadMore: Boolean)
    fun requestLimitLayout(isLoadMore: Boolean)
    fun emptyUserLayout(isLoadMore: Boolean)
    fun showErrorToast(error: ErrorConst.GithubError, isLoadMore: Boolean)
    fun invalidQueryLayout()
}