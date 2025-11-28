package com.dominio.bloommind.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Este Factory ya no necesita EmotionRepository porque el ViewModel no lo pide en el constructor.
// Mantenemos la clase para no romper código en las vistas, pero simplificamos su lógica.
class CheckInViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckInViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
