package com.dominio.bloommind.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.data.EmotionRepository
import com.dominio.bloommind.data.datastore.ProfileRepository
import com.dominio.bloommind.data.datastore.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Loading : ProfileState()
    data class LoggedIn(val userProfile: UserProfile) : ProfileState()
    object NotLoggedIn : ProfileState()
}

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val emotionRepository: EmotionRepository
) : ViewModel() {

    val profileState: StateFlow<ProfileState> = profileRepository.userProfileFlow
        .map { userProfile ->
            if (userProfile != null) {
                ProfileState.LoggedIn(userProfile)
            } else {
                ProfileState.NotLoggedIn
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ProfileState.Loading
        )

    fun updateProfile(name: String, email: String, birthDate: String, gender: String, iconId: String) {
        viewModelScope.launch {
            profileRepository.saveProfile(name, email, birthDate, gender, iconId)
        }
    }

    fun deleteProfile() {
        viewModelScope.launch {
            profileRepository.clearProfile()
            emotionRepository.clearEmotions()
        }
    }
}

class ProfileViewModelFactory(
    private val profileRepository: ProfileRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            val emotionRepository = EmotionRepository(context)
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(profileRepository, emotionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
