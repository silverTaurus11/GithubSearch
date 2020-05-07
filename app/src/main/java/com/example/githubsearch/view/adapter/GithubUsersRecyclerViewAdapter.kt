package com.example.githubsearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearch.R
import com.example.githubsearch.model.Repository

class GithubUsersRecyclerViewAdapter: RecyclerView.Adapter<GithubUsersRecyclerViewHolder>() {
    private var githubUsers: MutableList<Repository> = mutableListOf()
    private var isShowProgressBar = false

    fun insertNewDataUsers(newGithubUsers: List<Repository>){
        val result = DiffUtil.calculateDiff(MyCallBack(githubUsers,
            newGithubUsers.toMutableList()))
        this.githubUsers.clear()
        this.githubUsers.addAll(newGithubUsers)
        result.dispatchUpdatesTo(this)
    }

    fun clearDataUsers(){
        insertNewDataUsers(mutableListOf())
    }

    fun updateDataUsers(githubUsers: List<Repository>){
        this.githubUsers.addAll(githubUsers)
        notifyDataSetChanged()
    }

    fun showProgressBar(){
        if(itemCount > 0){
            isShowProgressBar = true
            notifyItemChanged(itemCount-1)
        }
    }

    fun hideProgressBar(){
        if(itemCount > 0){
            isShowProgressBar = false
            notifyItemChanged(itemCount-1)
        }
    }

    fun getItemList(): List<Repository>{
        return githubUsers
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GithubUsersRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.github_user_item,
            parent, false)

        return GithubUsersRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return githubUsers.size
    }

    override fun onBindViewHolder(holder: GithubUsersRecyclerViewHolder, position: Int) {
        holder.bind(githubUsers[position], position == itemCount-1, isShowProgressBar)
    }

}