package com.example.githubsearch.helper.network

import java.io.IOException

class NoInternetConnectionException: IOException(){
    override val message: String?
        get() = "No Internet Connection"
}