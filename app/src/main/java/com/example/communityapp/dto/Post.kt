package com.example.communityapp.dto

data class Post(
    var id: String? = null,
    var title: String,
    var content: String,
    var user: User? = null,
    var createdAt: Long
    ) : java.io.Serializable {
    constructor() : this("", "", "", null, System.currentTimeMillis())
}
