package com.example.communityapp.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.communityapp.R
import com.example.communityapp.databinding.ActivityJoinBinding
import com.example.communityapp.util.showToastMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "JoinActivity_테스트"

class JoinActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        auth = Firebase.auth

        binding.signUpBtn.setOnClickListener {
            createAccount(binding.signUpIdEdt.text.toString(), binding.signUpPassEdt.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            // 로그인 되어있을 시 메인화면 바로 이동
        }
    }

    private fun createAccount(email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        this.showToastMessage("계정 생성 성공")
                        finish()
                    } else {
                        this.showToastMessage(task.exception.toString())
                        Log.e(TAG, "failure", task.exception)
                    }
                }
        }
    }
}