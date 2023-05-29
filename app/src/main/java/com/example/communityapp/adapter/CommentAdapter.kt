package com.example.communityapp.adapter

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.communityapp.databinding.ItemCommentBinding
import com.example.communityapp.dto.Comment
import com.example.communityapp.util.CommonUtils

class CommentAdapter() : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    private var comments: List<Comment> = emptyList()

    fun setComments(comments: List<Comment>) {
        this.comments = comments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = comments[position]
        holder.contentView.text = item.content
        holder.userNameView.text = item.user!!.userName

        val timezone = "Asia/Tokyo"
        val dateTime = CommonUtils.convertMillisToTimezone(item.createdAt, timezone)
        val formattedDateTime = CommonUtils.formatLocalDateTime(dateTime, "yyyy-MM-dd HH:mm")
        holder.createdAtView.text = formattedDateTime

    }

    override fun getItemCount(): Int = comments.size

    inner class ViewHolder(binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.commentContent
        val userNameView: TextView = binding.commentUsername
        val createdAtView: TextView = binding.commentCreatedAt
    }

}