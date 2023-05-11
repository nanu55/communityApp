package com.example.communityapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.communityapp.R
import com.example.communityapp.adapter.ItemRecyclerViewAdapter
import com.example.communityapp.databinding.FragmentCommunityListBinding
import com.example.communityapp.dto.Post
import com.example.communityapp.fragment.placeholder.PlaceholderContent

class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityListBinding
    private var columnCount = 1
    private lateinit var list: List<Post>

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
        list = PlaceholderContent.ITEMS

        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemRecyclerViewAdapter(list)
//            adapter = ItemRecyclerViewAdapter(list)
        }
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