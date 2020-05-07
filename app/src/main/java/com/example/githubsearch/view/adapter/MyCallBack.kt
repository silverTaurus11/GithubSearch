package com.example.githubsearch.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.githubsearch.model.users.Owner

class MyCallBack(private val oldGithubUsers: MutableList<Owner>,
                 private val newGithubUsers: MutableList<Owner>): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldGithubUsers[oldItemPosition].id.equals(newGithubUsers[newItemPosition].id)
    }

    override fun getOldListSize(): Int {
        return oldGithubUsers.size
    }

    override fun getNewListSize(): Int {
        return newGithubUsers.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldGithubUsers[oldItemPosition] == newGithubUsers[newItemPosition]
    }
}