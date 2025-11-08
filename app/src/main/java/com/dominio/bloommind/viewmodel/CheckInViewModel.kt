package com.dominio.bloommind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.R
import com.dominio.bloommind.data.EmotionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Emotion(val nameResId: Int)

class CheckInViewModel : ViewModel() {

    val badEmotions = listOf(
        Emotion(R.string.emotion_anxious),
        Emotion(R.string.emotion_tired),
        Emotion(R.string.emotion_confused),
        Emotion(R.string.emotion_guilty),
        Emotion(R.string.emotion_disappointed),
        Emotion(R.string.emotion_stressed),
        Emotion(R.string.emotion_annoyed),
        Emotion(R.string.emotion_worried),
        Emotion(R.string.emotion_overwhelmed),
        Emotion(R.string.emotion_sad)
    )

    val okayEmotions = listOf(
        Emotion(R.string.emotion_bored),
        Emotion(R.string.emotion_calm),
        Emotion(R.string.emotion_comfortable),
        Emotion(R.string.emotion_indifferent)
    )

    val goodEmotions = listOf(
        Emotion(R.string.emotion_grateful),
        Emotion(R.string.emotion_at_ease),
        Emotion(R.string.emotion_excited),
        Emotion(R.string.emotion_entertained),
        Emotion(R.string.emotion_hopeful),
        Emotion(R.string.emotion_happy),
        Emotion(R.string.emotion_inspired),
        Emotion(R.string.emotion_peaceful),
        Emotion(R.string.emotion_satisfied),
        Emotion(R.string.emotion_serene)
    )

    private val _selectedEmotions = MutableStateFlow<Set<Emotion>>(emptySet())
    val selectedEmotions = _selectedEmotions.asStateFlow()

    fun toggleEmotionSelection(emotion: Emotion) {
        _selectedEmotions.update { currentSelection ->
            if (emotion in currentSelection) {
                currentSelection - emotion
            } else {
                if (currentSelection.size < 4) {
                    currentSelection + emotion
                } else {
                    currentSelection
                }
            }
        }
    }

    fun saveCheckIn(emotionRepository: EmotionRepository) {
        viewModelScope.launch {
            val emotionIds = _selectedEmotions.value.map { it.nameResId }.toSet()
            emotionRepository.saveCheckIn(emotionIds)
            clearSelection()
        }
    }

    private fun clearSelection() {
        _selectedEmotions.value = emptySet()
    }
}
