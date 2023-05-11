package com.example.communityapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.communityapp.R
import com.example.communityapp.activity.LoginActivity
import com.example.communityapp.activity.MainActivity
import com.example.communityapp.config.ApplicationClass
import com.example.communityapp.databinding.FragmentMypageBinding
import com.example.communityapp.dto.User
import com.example.communityapp.ui.viewmodel.MypageViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MypageFragment : Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private val model: MypageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        binding.apply {
            viewModel = model
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameEdt.visibility = View.GONE
        binding.nameSaveBtn.visibility = View.GONE
//        var user = ApplicationClass.sharedPreferencesUtil.getUser()
        binding.nameEdtBtn.setOnClickListener {
            binding.nameTv.visibility = View.GONE
            binding.nameEdt.visibility = View.VISIBLE
            binding.nameSaveBtn.visibility = View.VISIBLE
            binding.nameEdtBtn.visibility = View.GONE
        }

        binding.nameSaveBtn.setOnClickListener {
            binding.nameTv.visibility = View.VISIBLE
            binding.nameEdt.visibility = View.GONE
            binding.nameSaveBtn.visibility = View.GONE
            binding.nameEdtBtn.visibility = View.VISIBLE

            binding.viewModel?.nameChange()
            Log.i("클릭됨","")
        }

        binding.signOutBtn.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent)
        }
    }

}