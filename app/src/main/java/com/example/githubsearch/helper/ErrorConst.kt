package com.example.githubsearch.helper

object ErrorConst {
    const val GITHUB_REACH_REQUEST_LIMIT_CODE = 403
    const val GITHUB_INVALID_QUERY_CODE = 422
    enum class GithubError{
        NO_INTERNET_CONNECTION,
        SERVER_UNAVAILABLE,
        HAS_REACHED_BOTTOM_LIST
    }
}