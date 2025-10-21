package com.dominio.bloommind.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.ripple.rememberRipple // CORRECTED IMPORT
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dominio.bloommind.data.datastore.ProfileRepository
import com.dominio.bloommind.viewmodel.SignUpViewModel
import java.util.*

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
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        DatePickerField(
            label = "Fecha de Nacimiento",
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
            label = { Text("Email") },
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

@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            onDateSelected("$selectedDay/${selectedMonth + 1}/$selectedYear")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Seleccionar fecha",
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                    onClick = { datePickerDialog.show() }
                )
            )
        }
    )
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
        TextField(
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
