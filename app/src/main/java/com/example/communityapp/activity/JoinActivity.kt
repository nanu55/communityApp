package com.example.communityapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.communityapp.R
import com.example.communityapp.fragment.join.GenderSelectFragment
import com.example.communityapp.fragment.join.HeightSelectFragment
import com.example.communityapp.fragment.join.JoinCompletedFragment

private const val TAG = "JoinActivity_테스트"

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_join, GenderSelectFragment())
//            .addToBackStack(null)
            .commit()
    }
}