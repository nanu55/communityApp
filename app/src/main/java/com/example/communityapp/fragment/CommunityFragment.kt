package com.example.communityapp.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.communityapp.R
import com.example.communityapp.activity.NewPostActivity
import com.example.communityapp.adapter.PostAdapter
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.FragmentCommunityListBinding
import com.example.communityapp.dto.Post
import com.example.communityapp.viewmodel.MypageViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityListBinding
    private lateinit var postsAdapter: PostAdapter
    private lateinit var database: DatabaseReference
    private val model: MypageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_list, container, false)
        binding.apply {
            viewModel = model
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsAdapter = PostAdapter()
        binding.posts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postsAdapter
        }
        database = FirebaseManager.database.reference.child("posts")

        fetchPosts("")

        binding.searchEdt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()) {
                    binding.searchTextRemoveBtn.visibility = View.VISIBLE
                } else {
                    binding.searchTextRemoveBtn.visibility = View.GONE
                }
            }
        })

        binding.searchBtn.setOnClickListener {
            fetchPosts(binding.searchEdt.text.toString())
        }

        binding.searchTextRemoveBtn.setOnClickListener {
            binding.searchEdt.text.clear()
        }

        binding.floatingBtn.setOnClickListener {
            val intent = Intent(activity, NewPostActivity::class.java)
            intent.putExtra("user", model.user.value)
            startActivity(intent)
        }
    }

    private fun fetchPosts(keyword : String) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts: MutableList<Post> = mutableListOf()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let {
                        if(keyword.isEmpty()) posts.add(it)
                        else {
                            if(post.title.contains(keyword) || post.content.contains(keyword)) posts.add(it)
                            else {

                            }
                        }
                    }
                }
                postsAdapter.setPosts(posts)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}