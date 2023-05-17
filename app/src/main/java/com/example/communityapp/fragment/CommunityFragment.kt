package com.example.communityapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.communityapp.activity.NewPostActivity
import com.example.communityapp.adapter.PostAdapter
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.FragmentCommunityListBinding
import com.example.communityapp.dto.Post
import com.example.communityapp.ui.viewmodel.MypageViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityListBinding
    private lateinit var postsAdapter: PostAdapter
    private lateinit var database: DatabaseReference
    private val model: MypageViewModel by activityViewModels()
    private var columnCount = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityListBinding.inflate(inflater, container, false)
        return binding.root
//        val view = inflater.inflate(R.layout.fragment_community_list, container, false)
//
//        // Set the adapter
//        if (view is RecyclerView) {
//            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }
//                adapter = ItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
//            }
//        }
//        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        list = PlaceholderContent.ITEMS

        postsAdapter = PostAdapter()
        binding.posts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postsAdapter
        }
        database = FirebaseManager.database.reference.child("posts")

        fetchPosts()

        binding.floatingBtn.setOnClickListener {
            val intent = Intent(activity, NewPostActivity::class.java)
            intent.putExtra("user", model.user.value)
            startActivity(intent)
        }
    }

    private fun fetchPosts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts: MutableList<Post> = mutableListOf()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let {
                        posts.add(it)
                    }
                }
                postsAdapter.setPosts(posts)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CommunityFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}