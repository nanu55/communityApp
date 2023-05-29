package com.example.communityapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.communityapp.adapter.ChatAdapter
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.FragmentChatBinding
import com.example.communityapp.dto.ChatMessage
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chats: MutableList<ChatMessage>
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.chats.layoutManager = LinearLayoutManager(requireContext())
        chats = mutableListOf()
        chatAdapter = ChatAdapter(requireContext())
        chatAdapter.setChats(chats)
        binding.chats.adapter = chatAdapter
        database = FirebaseManager.database.reference.child("chats")

        loadChatMessages()

        binding.chatWriteBtn.setOnClickListener {
            sendButtonClicked()
        }
        return binding.root
    }

    private fun loadChatMessages() {
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                chatMessage?.let {
                    chats.add(it)
                    chatAdapter.notifyDataSetChanged()
                    binding.chats.scrollToPosition(chats.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun sendButtonClicked() {
        val message = binding.chatEdt.text.toString().trim()
        if (message.isNotEmpty()) {
            sendMessage(message)
            binding.chatEdt.text.clear()
        }
    }

    private fun sendMessage(message: String) {
        val userId = FirebaseManager.auth.currentUser!!.uid
        val timestamp = System.currentTimeMillis()

        if (userId != null) {
            val chatMessage = ChatMessage(userId, message, timestamp)
            val chatRef = database.push()
            chatRef.setValue(chatMessage)
        }
    }
}