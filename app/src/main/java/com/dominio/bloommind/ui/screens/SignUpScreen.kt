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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dominio.bloommind.R
import com.dominio.bloommind.data.datastore.ProfileRepository
import com.dominio.bloommind.ui.components.DatePickerField
import com.dominio.bloommind.ui.components.GenderDropdown
import com.dominio.bloommind.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    profileRepository: ProfileRepository,
    signUpViewModel: SignUpViewModel = viewModel(),
    iconId: String,
    onSignUpComplete: () -> Unit
) {
    val name by signUpViewModel.name.collectAsState()
    val email by signUpViewModel.email.collectAsState()
    val birthDate by signUpViewModel.birthDate.collectAsState()
    val gender by signUpViewModel.gender.collectAsState()

    val nameErrorId by signUpViewModel.nameError.collectAsState()
    val emailErrorId by signUpViewModel.emailError.collectAsState()
    val birthDateErrorId by signUpViewModel.birthDateError.collectAsState()

    val nameError = nameErrorId?.let { stringResource(id = it) }
    val emailError = emailErrorId?.let { stringResource(id = it) }
    val birthDateError = birthDateErrorId?.let { stringResource(id = it) }

    val isFormValid = name.isNotBlank() && email.isNotBlank() && birthDate.isNotBlank() && gender.isNotBlank() && nameError == null && emailError == null && birthDateError == null

    UserDetailsStep(
        name = name,
        onNameChange = { signUpViewModel.onNameChange(it) },
        nameError = nameError,
        email = email,
        onEmailChange = { signUpViewModel.onEmailChange(it) },
        emailError = emailError,
        birthDate = birthDate,
        onBirthDateChange = { signUpViewModel.onBirthDateChange(it) },
        birthDateError = birthDateError,
        gender = gender,
        onGenderChange = { signUpViewModel.onGenderChange(it) },
        onSignUpClicked = {
            if (isFormValid) {
                signUpViewModel.onSignUpClicked(profileRepository, iconId, onSignUpComplete)
            }
        },
        isButtonEnabled = isFormValid
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
