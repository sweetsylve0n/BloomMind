package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dominio.bloommind.data.datastore.ProfileRepository
import com.dominio.bloommind.viewmodel.SignUpViewModel
@Composable
fun SignUpScreen(
    onSignUpComplete: () -> Unit,
    profileRepository: ProfileRepository
) {
    val signUpViewModel: SignUpViewModel = viewModel()
    val name by signUpViewModel.name.collectAsState()
    val email by signUpViewModel.email.collectAsState()
    val birthDate by signUpViewModel.birthDate.collectAsState()
    val gender by signUpViewModel.gender.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Crea tu perfil", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { signUpViewModel.onNameChange(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = birthDate,
            onValueChange = { signUpViewModel.onBirthDateChange(it) },
            label = { Text("Fecha de Nacimiento (DD/MM/AAAA)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        GenderDropdown(
            selectedGender = gender,
            onGenderSelected = { signUpViewModel.onGenderChange(it) }
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { signUpViewModel.onEmailChange(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                signUpViewModel.onSignUpClicked(profileRepository, onSignUpComplete)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar y continuar")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(selectedGender: String, onGenderSelected: (String) -> Unit) {
    val genderOptions = listOf("She/Her", "He/Him", "Other")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedGender,
            onValueChange = {},
            readOnly = true,
            label = { Text("Género") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genderOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onGenderSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}