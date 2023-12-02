package com.example.legal_bridge.ui.chatbot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class chatbotViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is chatbot Fragment"
    }
    val text: LiveData<String> = _text
}