package com.example.communityapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.communityapp.R
import com.example.communityapp.activity.LoginActivity
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.FragmentMypageBinding
import com.example.communityapp.dto.Post
import com.example.communityapp.dto.User
import com.example.communityapp.ui.viewmodel.MypageViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class MypageFragment : Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private val model: MypageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        binding.apply {
            viewModel = model
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameEdt.visibility = View.GONE
        binding.nameSaveBtn.visibility = View.GONE
        binding.nameEdtBtn.setOnClickListener {
            binding.nameTv.visibility = View.GONE
            binding.nameEdt.visibility = View.VISIBLE
            binding.nameSaveBtn.visibility = View.VISIBLE
            binding.nameEdtBtn.visibility = View.GONE
        }

        binding.nameSaveBtn.setOnClickListener {
            binding.nameTv.visibility = View.VISIBLE
            binding.nameEdt.visibility = View.GONE
            binding.nameSaveBtn.visibility = View.GONE
            binding.nameEdtBtn.visibility = View.VISIBLE

            binding.viewModel?.nameChange()

//            val db = FirebaseManager.database.reference.child("users").child(FirebaseManager.auth.currentUser!!.uid)
//            db.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val user = dataSnapshot.getValue<User>()
//                    user!!.userName = model.user.value?.userName.toString()
//                    FirebaseManager.database.reference.child("users").child(FirebaseManager.auth.currentUser!!.uid)
//                        .setValue(user)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
            updateUserProfile(model.user.value!!.uid, model.user.value!!.userName, model.user.value!!.profileImageUrl)
        }

        // sign out
        binding.signOutBtn.setOnClickListener {
            FirebaseManager.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent)
        }

        binding.nameTv.text = model.user.value?.userName

        activity?.let {
            Glide.with(it)
                .load(model.user.value?.profileImageUrl.toString())
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.ic_error)
                .into(binding.myPageProfileImageIv)

        }
    }

    // 작성자 프로필 업데이트 시 해당 게시물의 작성자 정보 업데이트
    private fun updateUserProfile(userId: String, username: String, imageUrl: String) {
        val userRef = FirebaseManager.database.reference.child("users").child(userId)

        // 사용자 프로필 업데이트
        userRef.child("userName").setValue(username)

        // 해당 사용자의 게시물 참조 업데이트
        val postsQuery = FirebaseManager.database.reference.child("posts")
        postsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let {
                        if(it.user!!.uid == userId) {
                            val updatedPost = it.copy(user = User(userId, username, imageUrl))
                            postSnapshot.ref.setValue(updatedPost)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 쿼리 중단 시 처리
            }
        })
    }
}