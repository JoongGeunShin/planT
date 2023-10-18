package com.example.plant.main_fragment.calendar.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogViewModel : ViewModel() {

    var inputText: MutableLiveData<String> = MutableLiveData<String>().apply{
        value=""
    }

//    fun getData(): MutableLiveData<String> = inputText
//
//    fun updateText(input: String) {
//        inputText.value = input
//    }
}