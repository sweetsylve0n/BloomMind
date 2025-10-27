package com.dominio.bloommind.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.data.AffirmationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class AffirmationViewModel(private val repository: AffirmationRepository) : ViewModel() {

    private val _affirmationState = MutableStateFlow<AffirmationUiState>(AffirmationUiState.Loading)
    val affirmationState = _affirmationState.asStateFlow()

    init {
        fetchAffirmation()
    }

    fun fetchAffirmation() {
        viewModelScope.launch {
            _affirmationState.value = AffirmationUiState.Loading
            repository.getDailyAffirmation()
                .onSuccess { affirmation ->
                    _affirmationState.value = AffirmationUiState.Success(affirmation)
                }
                .onFailure { e ->
                    _affirmationState.value = AffirmationUiState.Error(e.message ?: "Error desconocido")
                }
        }
    }
}

class AffirmationViewModelFactory(private val context: Context? = null) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = if (context != null) AffirmationRepository(context) else throw IllegalArgumentException("Se requiere Context para AffirmationRepository")
        if (modelClass.isAssignableFrom(AffirmationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AffirmationViewModel(repo) as T
        }
        throw IllegalArgumentException("Clase de ViewModel desconocida")
    }
}
