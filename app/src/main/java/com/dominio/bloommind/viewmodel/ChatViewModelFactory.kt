package com.dominio.bloommind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dominio.bloommind.data.datastore.ChatQuotaRepository
import com.dominio.bloommind.data.internet.GeminiService

class ChatViewModelFactory(
    private val quotaRepository: ChatQuotaRepository,
    private val geminiService: GeminiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(quotaRepository, geminiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
