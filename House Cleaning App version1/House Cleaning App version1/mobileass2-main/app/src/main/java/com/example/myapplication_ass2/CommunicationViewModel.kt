// MessageViewModel.kt
package com.example.myapplication_ass2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessageViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val dao = db.messageDao()

    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages

    init {
        viewModelScope.launch {
            dao.getAllMessages().collect { _messages.value = it }
        }
    }

    fun addMessage(sender: String, text: String) {
        viewModelScope.launch {
            dao.insertMessage(MessageEntity(sender = sender, text = text))
        }
    }
}
