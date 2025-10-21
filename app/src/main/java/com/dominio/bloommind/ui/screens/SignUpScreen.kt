package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

    UserDetailsStep(
        name = name,
        onNameChange = { signUpViewModel.onNameChange(it) },
        email = email,
        onEmailChange = { signUpViewModel.onEmailChange(it) },
        birthDate = birthDate,
        onBirthDateChange = { signUpViewModel.onBirthDateChange(it) },
        gender = gender,
        onGenderChange = { signUpViewModel.onGenderChange(it) },
        onSignUpClicked = { signUpViewModel.onSignUpClicked(profileRepository, iconId, onSignUpComplete) }
    )
}

@Composable
fun UserDetailsStep(
    name: String, onNameChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    birthDate: String, onBirthDateChange: (String) -> Unit,
    gender: String, onGenderChange: (String) -> Unit,
    onSignUpClicked: () -> Unit
) {
    val isButtonEnabled = name.isNotBlank() && email.isNotBlank() && birthDate.isNotBlank() && gender.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Dime un poco de ti", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre:") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        DatePickerField(
            label = "Fecha de Nacimiento:",
            selectedDate = birthDate,
            onDateSelected = onBirthDateChange
        )
        Spacer(modifier = Modifier.height(16.dp))

        GenderDropdown(
            selectedGender = gender,
            onGenderSelected = onGenderChange
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email:") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSignUpClicked,
            enabled = isButtonEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar y continuar")
        }
    }
}
