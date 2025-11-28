package com.dominio.bloommind.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.R
import com.dominio.bloommind.data.repository.AdviceRepository
import com.dominio.bloommind.data.repository.AffirmationRepository
import com.dominio.bloommind.data.repository.EmotionRepository
import com.dominio.bloommind.data.dto.AdviceDto
import com.dominio.bloommind.data.dto.AffirmationDto
import com.dominio.bloommind.domain.GetDailyAdviceUseCase
import com.dominio.bloommind.domain.GetDailyAffirmationUseCase
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

sealed interface TodaysEmotionsUiState {
    object Loading : TodaysEmotionsUiState
    data class Success(val emotions: Set<Int>) : TodaysEmotionsUiState
}

sealed interface LastCheckinsUiState {
    object Loading : LastCheckinsUiState
    data class Success(val entries: List<Pair<String, Set<Int>>>) : LastCheckinsUiState
    data class Error(val message: String) : LastCheckinsUiState
}

class HomeViewModel(
    private val getDailyAffirmationUseCase: GetDailyAffirmationUseCase,
    private val getDailyAdviceUseCase: GetDailyAdviceUseCase,
    private val emotionRepository: EmotionRepository,
    context: Context
) : ViewModel() {
    private val appContext = context.applicationContext

    private val _affirmationState = MutableStateFlow<AffirmationUiState>(AffirmationUiState.Loading)
    val affirmationState = _affirmationState.asStateFlow()

    private val _adviceState = MutableStateFlow<AdviceUiState>(AdviceUiState.Loading)
    val adviceState = _adviceState.asStateFlow()
    private val _todaysEmotionsState = MutableStateFlow<TodaysEmotionsUiState>(TodaysEmotionsUiState.Loading)
    val todaysEmotionsState = _todaysEmotionsState.asStateFlow()
    private val _lastCheckinsState = MutableStateFlow<LastCheckinsUiState>(LastCheckinsUiState.Loading)
    val lastCheckinsState = _lastCheckinsState.asStateFlow()

    init {
        fetchDailyAffirmation()
        fetchDailyAdvice()
        fetchTodaysEmotions()
    }

    fun fetchTodaysEmotions() {
        viewModelScope.launch {
            _todaysEmotionsState.value = TodaysEmotionsUiState.Loading
            val emotions = emotionRepository.getTodaysEmotions()
            _todaysEmotionsState.value = TodaysEmotionsUiState.Success(emotions)
        }
    }

    fun fetchLastCheckins(n: Int = 5) {
        viewModelScope.launch {
            _lastCheckinsState.value = LastCheckinsUiState.Loading
            try {
                val entries = emotionRepository.getLastNCheckIns(n)
                _lastCheckinsState.value = LastCheckinsUiState.Success(entries)
            } catch (e: Exception) {
                _lastCheckinsState.value = LastCheckinsUiState.Error(e.message ?: appContext.getString(R.string.error_unknown))
            }
        }
    }

    fun fetchDailyAffirmation() {
        viewModelScope.launch {
            _affirmationState.value = AffirmationUiState.Loading
            getDailyAffirmationUseCase()
                .onSuccess { affirmation ->
                    _affirmationState.value = AffirmationUiState.Success(affirmation)
                }
                .onFailure { error ->
                    val errorMessage = error.message ?: appContext.getString(R.string.error_unknown)
                    _affirmationState.value = AffirmationUiState.Error(errorMessage)
                }
        }
    }

    fun fetchDailyAdvice() {
        viewModelScope.launch {
            _adviceState.value = AdviceUiState.Loading
            getDailyAdviceUseCase()
                .onSuccess { advice ->
                    _adviceState.value = AdviceUiState.Success(advice)
                }
                .onFailure { error ->
                    val errorMessage = error.message ?: appContext.getString(R.string.error_unknown)
                    _adviceState.value = AdviceUiState.Error(errorMessage)
                }
        }
    }
}

class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val affirmationRepository = AffirmationRepository(context)
            val adviceRepository = AdviceRepository(context)
            val emotionRepository = EmotionRepository(context)
            val getDailyAffirmationUseCase = GetDailyAffirmationUseCase(affirmationRepository)
            val getDailyAdviceUseCase = GetDailyAdviceUseCase(adviceRepository)

            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                getDailyAffirmationUseCase,
                getDailyAdviceUseCase,
                emotionRepository,
                context.applicationContext
            ) as T
        }
        throw IllegalArgumentException(context.getString(R.string.error_unknown_viewmodel))
    }
}
