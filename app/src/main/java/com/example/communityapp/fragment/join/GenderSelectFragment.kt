package com.example.communityapp.fragment.join

import android.content.Context
import android.graphics.Paint.Join
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.communityapp.R
import com.example.communityapp.activity.JoinActivity
import com.example.communityapp.databinding.FragmentGenderSelectBinding
import com.example.communityapp.util.showSnackBarMessage

class GenderSelectFragment : Fragment() {
    private lateinit var binding: FragmentGenderSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gender_select, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var gender = 0
        binding.genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.gender_male -> gender = 1
                R.id.gender_female -> gender = 2
            }
        }

        // 다음 선택 사항인 키 입력 화면으로 이동
        binding.genderSelectButton.setOnClickListener {

            var fragment = HeightSelectFragment()
            var bundle = Bundle()
            bundle.putInt("gender", gender)
            fragment.arguments = bundle

            if(gender == 0) view.showSnackBarMessage("select gender")
            else {
                activity?.supportFragmentManager!!.beginTransaction()
                    .replace(R.id.frame_layout_join, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}