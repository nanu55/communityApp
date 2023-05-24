package com.example.communityapp.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.communityapp.R
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.ItemChatBinding
import com.example.communityapp.databinding.ItemChatOtherBinding
import com.example.communityapp.dto.ChatMessage
import com.example.communityapp.util.CommonUtils
import com.example.communityapp.util.CommonUtils.getUserById

class ChatAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chats: List<ChatMessage> = emptyList()
    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2
    private val mContext = context
    private var mustShowDateFlag = false
    private val timezone = "Asia/Tokyo"
    fun setChats(chats: List<ChatMessage>) {
        this.chats = chats
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val chat = chats[position]
        val currentUserId = FirebaseManager.auth.currentUser!!.uid
        if(chat.userId == currentUserId) {
            return VIEW_TYPE_MESSAGE_SENT
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == VIEW_TYPE_MESSAGE_SENT) {
            return SentMessageHolder(
                ItemChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        } else {
            return ReceivedMessageHolder(
                ItemChatOtherBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = chats[position]
        mustShowDateFlag = false

        if(position != 0) {
            val lastItem = chats[position - 1]

            val dateTime1 = CommonUtils.convertMillisToTimezone(item.createdAt, timezone)
            val formattedDateTime1 = CommonUtils.formatLocalDateTime(dateTime1, "yyyy-MM-dd")
            val dateTime2 = CommonUtils.convertMillisToTimezone(lastItem.createdAt, timezone)
            val formattedDateTime2 = CommonUtils.formatLocalDateTime(dateTime2, "yyyy-MM-dd")

            if(formattedDateTime1 != formattedDateTime2) mustShowDateFlag = true

        } else {
            mustShowDateFlag = true
        }

        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(item)
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(item)
        }
    }

    inner class SentMessageHolder(binding:  ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        val messageView: TextView = binding.textGchatMessageMe
        val dateView: TextView = binding.textGchatDateMe
        val timeView: TextView = binding.textGchatTimestampMe

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(message: ChatMessage) {
            messageView.text = message.message

            val dateTime = CommonUtils.convertMillisToTimezone(message.createdAt, timezone)
            val formattedDateTime = CommonUtils.formatLocalDateTime(dateTime, "HH:mm")
            timeView.text = formattedDateTime

            dateView.text = CommonUtils.formatLocalDateTime(dateTime, "MM/dd")
            if(!mustShowDateFlag) dateView.visibility = View.GONE
        }
    }

    inner class ReceivedMessageHolder(binding:  ItemChatOtherBinding) : RecyclerView.ViewHolder(binding.root) {
        val messageView: TextView = binding.textGchatMessageOther
        val dateView: TextView = binding.textGchatDateOther
        val timeView: TextView = binding.textGchatTimestampOther
        val nameView: TextView = binding.textGchatUserOther
        val profileImageView: ImageView = binding.imageGchatProfileOther

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(message: ChatMessage) {
            messageView.text = message.message

            val dateTime = CommonUtils.convertMillisToTimezone(message.createdAt, timezone)
            val formattedDateTime = CommonUtils.formatLocalDateTime(dateTime, "HH:mm")
            timeView.text = formattedDateTime

            dateView.text = CommonUtils.formatLocalDateTime(dateTime, "MM/dd")
            if(!mustShowDateFlag) dateView.visibility = View.GONE

            getUserById(message.userId) {user ->
                nameView.text = user!!.userName

                Glide.with(mContext)
                    .load(user!!.profileImageUrl)
                    .circleCrop()
                    .placeholder(R.drawable.loading_img)
                    .into(profileImageView)
            }
        }
    }

    override fun getItemCount(): Int = chats.size
}