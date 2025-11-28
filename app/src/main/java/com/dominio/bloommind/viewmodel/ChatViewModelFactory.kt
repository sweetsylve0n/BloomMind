package com.dominio.bloommind.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dominio.bloommind.data.datastore.ChatQuotaRepository
import com.dominio.bloommind.data.ChatHistoryRepository
import com.dominio.bloommind.domain.SendGeminiMessage

class ChatViewModelFactory(
    private val quotaRepository: ChatQuotaRepository,
    private val historyRepository: ChatHistoryRepository,
    private val sendGeminiMessage: SendGeminiMessage,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(quotaRepository, historyRepository, sendGeminiMessage, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
