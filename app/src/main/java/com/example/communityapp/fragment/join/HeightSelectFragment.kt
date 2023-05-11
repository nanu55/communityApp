package com.example.communityapp.fragment.join

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.communityapp.R
import com.example.communityapp.activity.JoinActivity
import com.example.communityapp.databinding.FragmentHeightSelectBinding
import com.example.communityapp.util.showSnackBarMessage

class HeightSelectFragment : Fragment() {
    private lateinit var binding: FragmentHeightSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_height_select, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gender = arguments?.getInt("gender")
        var height = 0

        binding.heightRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.height_150 -> height = 150
                R.id.height_160 -> height = 160
                R.id.height_170 -> height = 170
                R.id.height_180 -> height = 180
            }
        }

        binding.heightSelectButton.setOnClickListener {

            var fragment = JoinCompletedFragment()
            var bundle = Bundle()
            bundle.putInt("gender", gender!!)
            bundle.putInt("height", height)
            fragment.arguments = bundle

            if(height == 0) view.showSnackBarMessage("select height")
            else {
                activity?.supportFragmentManager!!.beginTransaction()
                    .replace(R.id.frame_layout_join, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}