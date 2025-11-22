package com.dominio.bloommind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.R
import com.dominio.bloommind.data.datastore.ProfileRepository
import com.dominio.bloommind.ui.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _birthDate = MutableStateFlow("")
    val birthDate = _birthDate.asStateFlow()

    private val _gender = MutableStateFlow("")
    val gender = _gender.asStateFlow()

    // Validation error states are managed in the UI (SignUpScreen) using ValidationUtils directly
    // to keep the logic consistent between SignUp and Profile.
    // But if you want to keep state here, we can add it back.
    // For now, the UI observes these values and calls ValidationUtils itself, 
    // or we can expose the error state here.
    // Given the refactor in SignUpScreen to use ValidationUtils locally, we'll keep this simple.

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
        // Double check validation before saving
        if (ValidationUtils.isNameValid(_name.value) &&
            ValidationUtils.isEmailValid(_email.value) &&
            ValidationUtils.isBirthDateValid(_birthDate.value) &&
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
