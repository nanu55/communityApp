package com.example.communityapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MypageViewModel : ViewModel() {
    val name: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val inputText = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            Log.i("name_", "test")
            name.value = "test"
            inputText.value = name.value
        }
    }

    fun nameChange() {
        name.value = inputText.value.toString()
    }
}