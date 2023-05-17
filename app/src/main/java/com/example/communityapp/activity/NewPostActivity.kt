package com.example.communityapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.communityapp.R
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.ActivityNewPostBinding
import com.example.communityapp.dto.Post
import com.example.communityapp.dto.User
import com.example.communityapp.util.showToastMessage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.sql.Time

class NewPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPostBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_post)
        database = FirebaseManager.database.reference.child("posts")


        val user = intent.getSerializableExtra("user")

        binding.postSaveBtn.setOnClickListener {
            val post = Post()
            post.title = binding.postTitleEdt.text.toString()
            post.content = binding.postContentEdt.text.toString()
            post.user = user as User
            writePost(post)
            finish()
        }



    }

    private fun writePost(post: Post) {
        val postRef = database.push()
        postRef.setValue(post)
            .addOnSuccessListener {
                this.showToastMessage("post write success")
            }
            .addOnFailureListener {
                // 게시물 작성 실패
            }
    }
}