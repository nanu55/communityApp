package com.example.communityapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.dto.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.launch

class MypageViewModel : ViewModel() {
    val user: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    val inputText = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            FirebaseManager.auth.currentUser?.let {
                val uid = it.uid

                val reference = FirebaseManager.database.reference.child("users").child(uid)

                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue<User>()
                        user.value = value
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }

    fun nameChange() {
        user.value?.userName = inputText.value.toString()
    }

    fun imageUrlChange(newUrl: String) {
        user.value!!.profileImageUrl = newUrl
    }
}