package com.dominio.bloommind.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.R
import com.dominio.bloommind.data.AffirmationRepository
import com.dominio.bloommind.data.AdviceRepository
import com.dominio.bloommind.data.dto.AdviceDto
import com.dominio.bloommind.data.dto.AffirmationDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AffirmationUiState {
    object Loading : AffirmationUiState
    data class Success(val affirmation: AffirmationDto) : AffirmationUiState
    data class Error(val message: String) : AffirmationUiState
}

sealed interface AdviceUiState {
    object Loading : AdviceUiState
    data class Success(val advice: AdviceDto) : AdviceUiState
    data class Error(val message: String) : AdviceUiState
}

class HomeViewModel(private val context: Context) : ViewModel() {

    private val affirmationRepository = AffirmationRepository(context)
    private val adviceRepository = AdviceRepository(context)

    private val _affirmationState = MutableStateFlow<AffirmationUiState>(AffirmationUiState.Loading)
    val affirmationState = _affirmationState.asStateFlow()

    private val _adviceState = MutableStateFlow<AdviceUiState>(AdviceUiState.Loading)
    val adviceState = _adviceState.asStateFlow()

    init {
        fetchDailyAffirmation()
        fetchDailyAdvice()
    }

    fun fetchDailyAffirmation() {
        viewModelScope.launch {
            _affirmationState.value = AffirmationUiState.Loading
            affirmationRepository.getDailyAffirmation()
                .onSuccess { affirmation ->
                    _affirmationState.value = AffirmationUiState.Success(affirmation)
                }
                .onFailure { error ->
                    val errorMessage = error.message ?: context.getString(R.string.error_unknown)
                    _affirmationState.value = AffirmationUiState.Error(errorMessage)
                }
        }
    }

    fun fetchDailyAdvice() {
        viewModelScope.launch {
            _adviceState.value = AdviceUiState.Loading
            adviceRepository.getDailyAdvice()
                .onSuccess { advice ->
                    _adviceState.value = AdviceUiState.Success(advice)
                }
                .onFailure { error ->
                    val errorMessage = error.message ?: context.getString(R.string.error_unknown)
                    _adviceState.value = AdviceUiState.Error(errorMessage)
                }
        }
    }
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(context) as T
        }
        throw IllegalArgumentException(context.getString(R.string.error_unknown_viewmodel))
    }
}
