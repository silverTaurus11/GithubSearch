package com.example.githubsearch.view.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubsearch.R
import com.example.githubsearch.model.Repository


class GithubUsersRecyclerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val userImage by lazy { view.findViewById<ImageView>(R.id.userImage) }
    private val userName by lazy { view.findViewById<TextView>(R.id.userName) }
    private val parentViewLayout by lazy { view.findViewById<ConstraintLayout>(R.id.parentViewLayout) }
    private val progressBar by lazy { view.findViewById<ProgressBar>(R.id.loadMoreProgressBar) }
    private val imageRequestManager by lazy { Glide.with(view) }

    fun bind(item: Repository, isLastItem: Boolean, isShowFooterProgressBar: Boolean){
        imageRequestManager.load(item.owner.avatarUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(userImage)
        userName.text = item.name

        parentViewLayout.setOnClickListener { it?.let{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse(item.owner.htmlUrl)
            it.context.startActivity(intent)
        } }

        if(isLastItem && isShowFooterProgressBar){
            progressBar.visibility = View.VISIBLE
        } else{
            progressBar.visibility = View.GONE
        }
    }
}