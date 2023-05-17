package com.example.communityapp.ui.viewmodel

import android.util.Log
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

private const val TAG = "ViewModel_테스트"
class MypageViewModel : ViewModel() {
//    val name: MutableLiveData<String> by lazy { MutableLiveData<String>() }
//    val imageUri: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val user: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    val inputText = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            Log.i(TAG, "viewModel created")
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

//                val databaseUserName = FirebaseManager.database.reference.child("users").child(uid).child("userName")
//                val databaseProfileImageUri = FirebaseManager.database.reference.child("users").child(uid).child("profileImageUrl")
//
//                databaseUserName.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        val value = dataSnapshot.getValue<String>()
//                        name.value = value.toString()
//                        inputText.value = name.value
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//                })
//
//                databaseProfileImageUri.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        val value = dataSnapshot.getValue<String>()
//                        imageUri.value = value.toString()
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//                })
            }
        }
    }

    fun nameChange() {
        user.value?.userName = inputText.value.toString()
//        val profileUpdates = userProfileChangeRequest {
//            displayName = name.value
////                photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
//        }
//
//        FirebaseManager.auth.currentUser!!.updateProfile(profileUpdates)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.i(TAG, "profile updated " + name.value)
//                }
//            }
    }
}