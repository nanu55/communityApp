package com.example.communityapp.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.communityapp.activity.PostActivity

import com.example.communityapp.fragment.placeholder.PlaceholderContent.PlaceholderItem
import com.example.communityapp.databinding.FragmentCommunityBinding
import com.example.communityapp.dto.Post
import com.example.communityapp.fragment.CommunityFragment

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ItemRecyclerViewAdapter(
    private val values: List<Post>
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentCommunityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleView.text = item.title
        holder.userNameView.text = item.userName

        holder.postView.setOnClickListener {
            val intent = Intent(it.context, PostActivity::class.java)
            intent.putExtra("post_id", item.id)
            (it.context).startActivity(intent)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCommunityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.postTitle
        val userNameView: TextView = binding.postUsername
        val postView: LinearLayout = binding.post
    }

}