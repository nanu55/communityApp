package com.example.communityapp.dto

data class Comment(
    var id: String? = null,
    var content: String,
    var user: User? = null,
    var createdAt: Long
    ) : java.io.Serializable {
    constructor() : this("", "", null, System.currentTimeMillis())
}
