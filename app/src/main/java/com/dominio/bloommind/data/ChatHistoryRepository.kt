package com.dominio.bloommind.data

import android.content.Context
import com.dominio.bloommind.data.dto.GeminiContent
import com.dominio.bloommind.viewmodel.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ChatSessionData(
    val uiMessages: List<Message> = emptyList(),
    val apiHistory: List<GeminiContent> = emptyList()
)

class ChatHistoryRepository(private val context: Context) {
    private val gson = Gson()
    private val fileName = "bloom_chat_history.json"
    
    // Límite de mensajes para mantener el rendimiento y no saturar a Gemini
    private val MAX_HISTORY_LENGTH = 75

    suspend fun saveSession(uiMessages: List<Message>, apiHistory: List<GeminiContent>) {
        withContext(Dispatchers.IO) {
            try {
                // Lógica de recorte (Sliding Window):
                // Si superamos el límite, conservamos solo los últimos N mensajes.
                // Esto evita que el archivo crezca infinitamente.
                val trimmedUiMessages = if (uiMessages.size > MAX_HISTORY_LENGTH) {
                    uiMessages.takeLast(MAX_HISTORY_LENGTH)
                } else {
                    uiMessages
                }

                val trimmedApiHistory = if (apiHistory.size > MAX_HISTORY_LENGTH) {
                    apiHistory.takeLast(MAX_HISTORY_LENGTH)
                } else {
                    apiHistory
                }

                val sessionData = ChatSessionData(trimmedUiMessages, trimmedApiHistory)
                val json = gson.toJson(sessionData)
                val file = File(context.filesDir, fileName)
                file.writeText(json)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getSession(): ChatSessionData {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, fileName)
                if (!file.exists()) return@withContext ChatSessionData()

                val json = file.readText()
                val type = object : TypeToken<ChatSessionData>() {}.type
                gson.fromJson(json, type) ?: ChatSessionData()
            } catch (e: Exception) {
                e.printStackTrace()
                ChatSessionData()
            }
        }
    }

    suspend fun clearHistory() {
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, fileName)
            if (file.exists()) file.delete()
        }
    }
}
