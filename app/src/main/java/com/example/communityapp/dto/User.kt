package com.example.communityapp.dto


data class User(
    var uid: String,
    var userName: String,
    var profileImageUrl: String

) : java.io.Serializable {
    constructor() : this("", "", "")
    constructor(profileImageUrl: String, uid: String) : this(profileImageUrl, uid, "")
}

// http://mobile-pjt.sample.ssafy.io/rest/user/info?id=iop90