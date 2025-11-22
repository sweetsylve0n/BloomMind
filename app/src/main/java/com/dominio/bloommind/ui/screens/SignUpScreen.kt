package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dominio.bloommind.R
import com.dominio.bloommind.data.datastore.ProfileRepository
import com.dominio.bloommind.ui.components.DatePickerField
import com.dominio.bloommind.ui.components.GenderDropdown
import com.dominio.bloommind.viewmodel.SignUpViewModel
import com.dominio.bloommind.ui.utils.ValidationUtils

@Composable
fun SignUpScreen(
    profileRepository: ProfileRepository,
    signUpViewModel: SignUpViewModel = viewModel(),
    iconId: String,
    onSignUpComplete: () -> Unit
) {
    val context = LocalContext.current

    val name by signUpViewModel.name.collectAsState()
    val email by signUpViewModel.email.collectAsState()
    val birthDate by signUpViewModel.birthDate.collectAsState()
    val gender by signUpViewModel.gender.collectAsState()
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var birthDateError by remember { mutableStateOf<String?>(null) }

    // Función local para validar todos los campos
    fun validateAll(): Boolean {
        var isValid = true
        
        if (name.isBlank()) {
            nameError = context.getString(R.string.error_empty_name)
            isValid = false
        } else if (!ValidationUtils.isNameValid(name)) {
            nameError = context.getString(R.string.error_invalid_name)
            isValid = false
        } else {
            nameError = null
        }

        if (email.isBlank()) {
            emailError = context.getString(R.string.error_empty_email)
            isValid = false
        } else if (!ValidationUtils.isEmailValid(email)) {
            emailError = context.getString(R.string.error_invalid_email)
            isValid = false
        } else {
            emailError = null
        }

        if (birthDate.isBlank()) {
            birthDateError = context.getString(R.string.error_empty_birthdate)
            isValid = false
        } else if (!ValidationUtils.isBirthDateValid(birthDate)) {
            birthDateError = context.getString(R.string.error_invalid_birthdate)
            isValid = false
        } else {
            birthDateError = null
        }
        
        if (gender.isBlank()) {
             isValid = false
        }

        return isValid
    }
    
    // Verificamos validez en tiempo real para habilitar/deshabilitar botón si se desea,
    // aunque es mejor dejar el botón habilitado y mostrar errores al clickear.
    // Aquí usaremos la estrategia de validar al cambiar para mostrar errores inmediatos.

    UserDetailsStep(
        name = name,
        onNameChange = {
            signUpViewModel.onNameChange(it)
            // Validación inmediata
            nameError = if (it.isBlank()) null // No mostrar error mientras escribe vacio al inicio
            else if (!ValidationUtils.isNameValid(it)) context.getString(R.string.error_invalid_name)
            else null
        },
        nameError = nameError,
        email = email,
        onEmailChange = {
            signUpViewModel.onEmailChange(it)
            emailError = if (it.isBlank()) null 
            else if (!ValidationUtils.isEmailValid(it)) context.getString(R.string.error_invalid_email)
            else null
        },
        emailError = emailError,
        birthDate = birthDate,
        onBirthDateChange = {
            signUpViewModel.onBirthDateChange(it)
            birthDateError = if (it.isBlank()) null
            else if (!ValidationUtils.isBirthDateValid(it)) context.getString(R.string.error_invalid_birthdate)
            else null
        },
        birthDateError = birthDateError,
        gender = gender,
        onGenderChange = { signUpViewModel.onGenderChange(it) },
        onSignUpClicked = {
            if (validateAll()) {
                signUpViewModel.onSignUpClicked(profileRepository, iconId, onSignUpComplete)
            }
        },
        // Habilitamos el botón solo si no hay errores visibles y los campos no están vacíos
        isButtonEnabled = true // Dejamos siempre habilitado para que el usuario reciba feedback al presionar
    )
}

@Composable
fun UserDetailsStep(
    name: String, onNameChange: (String) -> Unit, nameError: String?,
    email: String, onEmailChange: (String) -> Unit, emailError: String?,
    birthDate: String, onBirthDateChange: (String) -> Unit, birthDateError: String?,
    gender: String, onGenderChange: (String) -> Unit,
    onSignUpClicked: () -> Unit,
    isButtonEnabled: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.signup_intro), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(stringResource(id = R.string.label_name)) },
            modifier = Modifier.fillMaxWidth(),
            isError = nameError != null,
            supportingText = { if (nameError != null) Text(nameError) },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        DatePickerField(
            label = stringResource(id = R.string.label_birthdate),
            selectedDate = birthDate,
            onDateSelected = onBirthDateChange,
            isError = birthDateError != null,
            errorMessage = birthDateError
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(id = R.string.label_email)) },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError != null,
            supportingText = { if (emailError != null) Text(emailError) },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        GenderDropdown(
            selectedGender = gender,
            onGenderSelected = onGenderChange
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSignUpClicked,
            enabled = isButtonEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.signup_save_continue))
        }
    }
}
