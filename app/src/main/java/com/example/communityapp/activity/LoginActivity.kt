package com.example.communityapp.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.communityapp.R
import com.example.communityapp.config.ApplicationClass
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.communityapp.util.showToastMessage
import com.google.firebase.auth.FirebaseUser
import java.util.jar.Manifest

private const val TAG = "LoginActivity_테스트"

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private final val permission_code = 0
    private val permission = {
        android.Manifest.permission.CAMERA;
        android.Manifest.permission.READ_EXTERNAL_STORAGE;
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // 권한 체크
        checkPermission()

        binding.btnLogin.setOnClickListener {
            signIn(binding.editTextLoginID.text.toString(), binding.editTextLoginPW.text.toString())
        }

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseManager.auth.currentUser
        if(currentUser != null){
            reload(currentUser)
        }
    }

    private fun reload(user: FirebaseUser) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null) {
            reload(user)
        }
    }

    private fun signIn(email: String, password: String) {
        if(email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseManager.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = FirebaseManager.auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        this.showToastMessage("sign in failed")
                        updateUI(null)
                    }
                }
        } else {
            this.showToastMessage("fill email and password")
        }
    }

    //권한 확인
    private fun checkPermission() {

        // 1. 위험권한(Camera) 권한 승인상태 가져오기
        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            // 카메라 권한이 승인된 상태일 경우
            startProcess()

        } else {
            // 카메라 권한이 승인되지 않았을 경우
            requestPermission()
        }
    }

    // 2. 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 99)
    }

    // 권한 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            99 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startProcess()
                } else {
                    Log.d("MainActivity", "종료")
                }
            }
        }
    }

    // 3. 카메라 기능 실행
    private fun startProcess() {
//        this.showToastMessage("카메라 on")
    }
}