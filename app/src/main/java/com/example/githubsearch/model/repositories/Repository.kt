package com.example.githubsearch.model.repositories

import com.example.githubsearch.model.users.Owner
import com.google.gson.annotations.SerializedName

data class Repository(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("owner") val owner: Owner,
    @SerializedName("url") val url: String,
    @SerializedName("created_at") val createdDate: String
)