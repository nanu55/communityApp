package com.example.communityapp.activity

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.communityapp.R
import com.example.communityapp.config.FirebaseManager
import com.example.communityapp.databinding.ActivityMainBinding
import com.example.communityapp.fragment.ChatFragment
import com.example.communityapp.fragment.CommunityFragment
import com.example.communityapp.fragment.HomeFragment
import com.example.communityapp.fragment.MypageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity_테스트"

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowTitleEnabled(false)


        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_main, HomeFragment())
            .commit()

        bottomNavigation = binding.tabLayoutBottomNavigation
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_page_1 -> {
                    binding.toolbarTitleTv.text = "ホームページ"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, HomeFragment())
                        .commit()
                    true
                }
                R.id.navigation_page_2 -> {
                    binding.toolbarTitleTv.text = "チャット"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, ChatFragment())
                        .commit()
                    true
                }
                R.id.navigation_page_3 -> {
                    binding.toolbarTitleTv.text = "コミュニティ"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, CommunityFragment())
                        .commit()
                    true
                }
                R.id.navigation_page_4 -> {
                    binding.toolbarTitleTv.text = "マイページ"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, MypageFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

        bottomNavigation.setOnNavigationItemReselectedListener { item ->
            // 재선택시 다시 랜더링 하지 않기 위해 수정
            if (bottomNavigation.selectedItemId != item.itemId) {
                bottomNavigation.selectedItemId = item.itemId
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.toolBarSignOut -> {
                FirebaseManager.auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}