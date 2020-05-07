package com.example.githubsearch.model

import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("id") val id: String,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("avatar_url") val avatarUrl: String
)