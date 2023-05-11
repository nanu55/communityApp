package com.example.communityapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.communityapp.fragment.LoginFragment
import com.example.communityapp.R
import com.example.communityapp.databinding.ActivityLoginBinding

private const val TAG = "LoginActivity_테스트"

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_login, LoginFragment())
            .commit()


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


    fun openFragment(int: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        when (int) {
            1 -> {
                val intent = Intent(this, MainActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, JoinActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
            }
            3 -> transaction.replace(R.id.frame_layout_login, LoginFragment())
                .addToBackStack(null)
        }
        transaction.commit()
    } // End of openFragment
} // End of LoginActivity class
