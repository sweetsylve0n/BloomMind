package com.dominio.bloommind.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dominio.bloommind.data.datastore.ChatQuotaRepository
import com.dominio.bloommind.data.repository.ChatHistoryRepository
import com.dominio.bloommind.domain.SendGeminiMessageUseCase

class ChatViewModelFactory(
    private val quotaRepository: ChatQuotaRepository,
    private val historyRepository: ChatHistoryRepository,
    private val sendGeminiMessageUseCase: SendGeminiMessageUseCase,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(quotaRepository, historyRepository, sendGeminiMessageUseCase, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
