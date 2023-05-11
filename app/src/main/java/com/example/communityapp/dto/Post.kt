package com.example.communityapp.dto

data class Post(
    var id: String,
    var title: String,
    var content: String,
    var userName: String,
    var commentCount: Int,
    var createdAt: Long

    ) : java.io.Serializable {
    constructor() : this("", "", "", "", 0, 0)
}