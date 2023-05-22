package com.example.communityapp.adapter

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.communityapp.databinding.ItemChatBinding
import com.example.communityapp.databinding.ItemCommentBinding
import com.example.communityapp.dto.ChatMessage
import com.example.communityapp.dto.Comment
import com.example.communityapp.dto.User
import com.example.communityapp.util.CommonUtils
import com.example.communityapp.util.CommonUtils.getUserById

class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private var chats: List<ChatMessage> = emptyList()

    fun setComments(chats: List<ChatMessage>) {
        this.chats = chats
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chats[position]
        getUserById(item.userId) { user ->
            if (user != null) {
                // user 객체 사용
                holder.chatUserNameView.text = user.userName
            } else {
                // 해당 user id에 대한 데이터가 없는 경우 처리
            }
        }
        holder.chatUserNameView.text = item.userId
        holder.chatMessageView.text = item.message

        val timezone = "Asia/Tokyo"
        val dateTime = CommonUtils.convertMillisToTimezone(item.timestamp, timezone)
        val formattedDateTime = CommonUtils.formatLocalDateTime(dateTime, "yyyy-MM-dd HH:mm")
        holder.chatTimeStampView.text = formattedDateTime

    }

    override fun getItemCount(): Int = chats.size

    inner class ViewHolder(binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val chatUserNameView: TextView = binding.chatUserNameTv
        val chatMessageView: TextView = binding.chatMessageTv
        val chatTimeStampView: TextView = binding.chatTimestampTv
    }

}