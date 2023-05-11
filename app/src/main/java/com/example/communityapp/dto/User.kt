package com.example.communityapp.dto


data class User(
    var id: String,
    var name: String,
    var pass: String,

) : java.io.Serializable {
    constructor() : this("", "", "")
    constructor(id: String, pass: String) : this(id, "", pass)
}

// http://mobile-pjt.sample.ssafy.io/rest/user/info?id=iop90