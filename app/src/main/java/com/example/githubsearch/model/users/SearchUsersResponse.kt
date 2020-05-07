package com.example.githubsearch.model.users

import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResult: Boolean,
    @SerializedName("items") val items: List<Owner>
)