package com.example.plant.main_fragment.calendar.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogViewModel : ViewModel() {

    private var inputText: MutableLiveData<String> = MutableLiveData()

    fun getData(): MutableLiveData<String> = inputText

    fun updateText(input: String) {
        inputText.value = input
    }
}