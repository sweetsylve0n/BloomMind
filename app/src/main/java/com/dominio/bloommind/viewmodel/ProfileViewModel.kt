package com.dominio.bloommind.viewmodel

import kotlinx.coroutines.flow.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.data.datastore.ProfileRepository
import com.dominio.bloommind.data.datastore.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Loading : ProfileState()
    data class LoggedIn(val userProfile: UserProfile) : ProfileState()
    object NotLoggedIn : ProfileState()
}

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

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
}

//la factoriaaaaa
class ProfileViewModelFactory(
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
