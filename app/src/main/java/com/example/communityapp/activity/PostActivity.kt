package com.example.communityapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.communityapp.R
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.ItemPostBinding
import com.example.communityapp.dto.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ItemPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post)

        val postId = intent.getStringExtra("post_id")
        val postRef = FirebaseManager.database.reference.child("posts").child(postId!!)

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<Post>()

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}