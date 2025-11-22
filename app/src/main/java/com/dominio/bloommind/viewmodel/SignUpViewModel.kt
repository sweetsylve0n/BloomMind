package com.dominio.bloommind.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dominio.bloommind.R
import com.dominio.bloommind.data.datastore.ProfileRepository
import com.dominio.bloommind.ui.utils.isDateInThePast
import com.dominio.bloommind.ui.utils.isEmailValid
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

    private val _nameError = MutableStateFlow<Int?>(null)
    val nameError = _nameError.asStateFlow()

    private val _emailError = MutableStateFlow<Int?>(null)
    val emailError = _emailError.asStateFlow()

    private val _birthDateError = MutableStateFlow<Int?>(null)
    val birthDateError = _birthDateError.asStateFlow()

    fun onNameChange(newName: String) {
        _name.update { newName }
        validateName(newName)
    }

    fun onEmailChange(newEmail: String) {
        _email.update { newEmail }
        validateEmail(newEmail)
    }

    fun onBirthDateChange(newDate: String) {
        _birthDate.update { newDate }
        validateBirthDate(newDate)
    }

    fun onGenderChange(newGender: String) {
        _gender.update { newGender }
    }

    //faltan validaciones para cada campo, que acepten los caracteres correctos, el nombre no puede acepta numeros, y demas
    private fun validateName(name: String) {
        _nameError.value = if (name.isBlank()) R.string.error_empty_name else null
    }

    private fun validateEmail(email: String) {
        _emailError.value = if (!isEmailValid(email)) R.string.error_invalid_email else null
    }

    private fun validateBirthDate(date: String) {
        _birthDateError.value = if (date.isNotBlank() && !isDateInThePast(date)) R.string.error_future_date else null
    }

    fun isFormValid(): Boolean {
        validateName(_name.value)
        validateEmail(_email.value)
        validateBirthDate(_birthDate.value)

        return _name.value.isNotBlank() &&
                _email.value.isNotBlank() &&
                _birthDate.value.isNotBlank() &&
                _gender.value.isNotBlank() &&
                _nameError.value == null &&
                _emailError.value == null &&
                _birthDateError.value == null
    }

    fun onSignUpClicked(profileRepository: ProfileRepository, iconId: String, onSignUpSuccess: () -> Unit) {
        if (isFormValid()) {
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
