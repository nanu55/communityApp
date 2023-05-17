package com.example.communityapp.fragment.placeholder

import com.example.communityapp.dto.Post
import com.example.communityapp.dto.User
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<Post> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, Post> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createPlaceholderItem(i))
        }
    }

    private fun addItem(item: Post) {
        ITEMS.add(item)
        item.id?.let { ITEM_MAP.put(it, item) }
    }

    private fun createPlaceholderItem(position: Int): Post {
        return Post(position.toString(), "Post " + position, makeDetails(position), User(), emptyList(), 0)
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
//        for (i in 0..position - 1) {
//            builder.append("\nMore details information here.")
//        }
        return builder.toString()
    }

    /**
     * A placeholder item representing a piece of content.
     */
    data class PlaceholderItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}