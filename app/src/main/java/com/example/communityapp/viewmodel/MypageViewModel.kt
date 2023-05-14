package com.example.communityapp.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.communityapp.util.showToastMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

private const val TAG = "ViewModel_테스트"
class MypageViewModel : ViewModel() {
    val name: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val imageUri: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val inputText = MutableLiveData<String>()
    val user = Firebase.auth.currentUser
    val database = Firebase.database

    init {
        viewModelScope.launch {
            Log.i(TAG, "viewModel created")
            user?.let {
                val uid = it.uid

                val databaseUserName = database.reference.child("users").child(uid).child("userName")
                val databaseProfileImageUri = database.reference.child("users").child(uid).child("profileImageUrl")

                databaseUserName.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        val value = dataSnapshot.getValue<String>()
                        name.value = value.toString()
                        inputText.value = name.value
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

                databaseProfileImageUri.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        val value = dataSnapshot.getValue<String>()
                        imageUri.value = value.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
//                name.value = database.reference.child("users").child(uid).child("userName").toString()
//                imageUri.value = database.reference.child("users").child(uid).child("profileImageUrl").toString()
            }


//            inputText.value = name.value
        }
    }

    fun nameChange() {
        name.value = inputText.value.toString()
        val profileUpdates = userProfileChangeRequest {
            displayName = name.value
//                photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "profile updated " + name.value)
                }
            }
    }
}