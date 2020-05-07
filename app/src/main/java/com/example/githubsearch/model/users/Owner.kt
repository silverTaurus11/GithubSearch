package com.example.githubsearch.model.users

import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("login") val name: String,
    @SerializedName("id") val id: String,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("avatar_url") val avatarUrl: String
)