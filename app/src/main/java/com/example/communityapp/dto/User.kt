package com.example.communityapp.dto


data class User(
    var uid: String,
    var userName: String,
    var profileImageUrl: String

) : java.io.Serializable {
    constructor() : this("", "", "")
}

// http://mobile-pjt.sample.ssafy.io/rest/user/info?id=iop90