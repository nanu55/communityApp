package com.example.communityapp.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.communityapp.R
import com.example.communityapp.adapter.CommentAdapter
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.ActivityPostBinding
import com.example.communityapp.dto.Comment
import com.example.communityapp.dto.Post
import com.example.communityapp.dto.User
import com.example.communityapp.util.CommonUtils
import com.example.communityapp.util.showToastMessage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var postRef: DatabaseReference
    private lateinit var commentRef: DatabaseReference
    private lateinit var postUserRef: DatabaseReference
    private lateinit var currentUserRef: DatabaseReference
    private lateinit var postUser: User
    private lateinit var currentUser: User
    private lateinit var postIdFromIntent: String
    private var countedFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post)

        postIdFromIntent = intent.getStringExtra("post_id")!!

        commentAdapter = CommentAdapter()
        binding.comments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
        }
        postRef = FirebaseManager.database.reference.child("posts").child(postIdFromIntent!!)
        commentRef = FirebaseManager.database.reference.child("comments")
        postUserRef = FirebaseManager.database.reference.child("posts").child(postIdFromIntent!!).child("user")
        currentUserRef = FirebaseManager.database.reference.child("users").child(FirebaseManager.auth.currentUser!!.uid)


        getCurrentUser()
        fetchPost()
        fetchComments()


        binding.commentWriteBtn.setOnClickListener {
            if(binding.commentEdt.text.toString() == "") {
                this.showToastMessage("input comment")
            } else {
                val comment = Comment()
                comment.apply {
                    content = binding.commentEdt.text.toString()
                    postId = postIdFromIntent!!
                    user = postUser
                }
                writeComment(comment)
                binding.commentEdt.text.clear()
            }
        }
    }

    private fun fetchPost() {
        postRef.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var post = dataSnapshot.getValue<Post>()

                if(!countedFlag) {
                    post = post!!.copy(viewCount = post!!.viewCount + 1)
                    postRef.setValue(post)
                    countedFlag = true
                }
                binding.postTitleTv.text = post!!.title
                binding.postUserNameTv.text = post!!.user!!.userName
                binding.postContentTv.text = post!!.content
                binding.postViewCountTv.text = "view count : " + post!!.viewCount

                val timezone = "Asia/Tokyo"
                val dateTime = CommonUtils.convertMillisToTimezone(post!!.createdAt, timezone)
                val formattedDateTime = CommonUtils.formatLocalDateTime(dateTime, "yyyy-MM-dd HH:mm")
                binding.postCreatedAtTv.text = formattedDateTime

                postUser = post.user!!
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun fetchComments() {
        commentRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments: MutableList<Comment> = mutableListOf()
                for (commentSnapshot in snapshot.children) {
                    val comment = commentSnapshot.getValue(Comment::class.java)
                    comment?.let {
                        if(it.postId == postIdFromIntent) {
                            comments.add(it)
                        }
                    }
                }
                binding.commentsCountTv.text = "comments: " + comments.size
                commentAdapter.setComments(comments)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun writeComment(comment: Comment) {
        val database = commentRef.push()
        val commentId = database.key
        if(currentUser != null) {
            database.setValue(comment.copy(id = commentId, user = currentUser))
                .addOnSuccessListener {
                    this.showToastMessage("comment write success")
                }
                .addOnFailureListener {
                }
        }
    }

    private fun getCurrentUser() {
        currentUserRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                currentUser = user!!
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}