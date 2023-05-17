package com.example.communityapp.dto

data class Post(
    var id: String? = null,
    var title: String,
    var content: String,
    var user: User? = null,
    var comments: List<Comment>,
    var createdAt: Long
    ) : java.io.Serializable {
    constructor() : this("", "", "", null, emptyList(), System.currentTimeMillis())
}
