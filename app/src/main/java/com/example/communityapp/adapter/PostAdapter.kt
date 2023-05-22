package com.example.communityapp.adapter

import android.content.Intent
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.communityapp.activity.PostActivity
import com.example.communityapp.databinding.ItemPostBinding
import com.example.communityapp.dto.Post
import com.example.communityapp.util.CommonUtils

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class PostAdapter() : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    private var posts: List<Post> = emptyList()

    fun setPosts(posts: List<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = posts[position]
        holder.titleView.text = item.title
        holder.userNameView.text = item.user!!.userName
        holder.viewCountView.text = "view count : " + item.viewCount

        val timezone = "Asia/Tokyo"
        val dateTime = CommonUtils.convertMillisToTimezone(item.createdAt, timezone)
        val formattedDateTime = CommonUtils.formatLocalDateTime(dateTime, "yyyy-MM-dd HH:mm")
        holder.createdAtView.text = formattedDateTime

        holder.postView.setOnClickListener {
            val intent = Intent(it.context, PostActivity::class.java)
            intent.putExtra("post_id", item.id)
            (it.context).startActivity(intent)
        }
    }

    override fun getItemCount(): Int = posts.size

    inner class ViewHolder(binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.postTitle
        val userNameView: TextView = binding.postUsername
        val viewCountView: TextView = binding.postViewCount
        val createdAtView: TextView = binding.postCreatedAt
        val postView: ConstraintLayout = binding.post
    }

}