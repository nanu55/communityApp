package com.example.communityapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.communityapp.R
import com.example.communityapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.communityapp.util.showToastMessage
import com.google.firebase.auth.FirebaseUser

private const val TAG = "LoginActivity_테스트"

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        auth = Firebase.auth
        binding.btnLogin.setOnClickListener {
            signIn(binding.editTextLoginID.text.toString(), binding.editTextLoginPW.text.toString())
        }

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }

//        //로그인 된 상태인지 확인
//        var user = sharedPreferencesUtil.getUser()
//
//        //로그인 상태 확인. id가 있다면 로그인 된 상태
//        if (user.id != "") {
//            openFragment(1)
//        } else {
//            // 가장 첫 화면은 홈 화면의 Fragment로 지정
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.frame_layout_login, LoginFragment())
//                .commit()
//        }


    } // End of onCreate


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload(currentUser)
        }
    }

    private fun reload(user: FirebaseUser) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null) {
            reload(user)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    this.showToastMessage("sign in failed")
                    updateUI(null)
                }
            }
    }
} // End of LoginActivity class
