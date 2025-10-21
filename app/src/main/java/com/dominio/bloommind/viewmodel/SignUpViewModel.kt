package com.dominio.bloommind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.data.datastore.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    // User details state
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _birthDate = MutableStateFlow("")
    val birthDate = _birthDate.asStateFlow()

    private val _gender = MutableStateFlow("")
    val gender = _gender.asStateFlow()

    // --- Event Handlers ---

    fun onNameChange(newName: String) {
        _name.update { newName }
    }

    fun onEmailChange(newEmail: String) {
        _email.update { newEmail }
    }

    fun onBirthDateChange(newDate: String) {
        _birthDate.update { newDate }
    }

    fun onGenderChange(newGender: String) {
        _gender.update { newGender }
    }

    fun onSignUpClicked(profileRepository: ProfileRepository, iconId: String, onSignUpSuccess: () -> Unit) {
        if (_name.value.isNotBlank() &&
            _email.value.isNotBlank() &&
            _birthDate.value.isNotBlank() &&
            _gender.value.isNotBlank()
        ) {
            viewModelScope.launch {
                profileRepository.saveProfile(
                    name = _name.value,
                    email = _email.value,
                    birthDate = _birthDate.value,
                    gender = _gender.value,
                    iconId = iconId
                )
                onSignUpSuccess()
            }
        }
    }
}