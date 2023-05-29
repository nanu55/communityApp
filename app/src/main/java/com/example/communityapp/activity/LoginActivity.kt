package com.example.communityapp.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.communityapp.R
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.ActivityLoginBinding
import com.example.communityapp.util.showToastMessage
import com.google.firebase.auth.FirebaseUser


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
            reload()
        }
    }

    private fun reload() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        this.showToastMessage("サインインに成功しました")
        startActivity(intent)
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null) {
            reload()
        }
    }

    private fun signIn(email: String, password: String) {
        if(email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseManager.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = FirebaseManager.auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        this.showToastMessage("サインインに失敗しました")
                        updateUI(null)
                    }
                }
        } else {
            this.showToastMessage("メールアドレスとパスワードを入力してください")
        }
    }

    //권한 확인
    private fun checkPermission() {
        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            startProcess()

        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 99)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            99 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startProcess()
                }
            }
        }
    }

    private fun startProcess() {
//        this.showToastMessage("カメラ使用を許可しました")
    }
}