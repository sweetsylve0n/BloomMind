package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dominio.bloommind.R
import com.dominio.bloommind.data.datastore.UserProfile
import com.dominio.bloommind.ui.components.DatePickerField
import com.dominio.bloommind.ui.components.GenderDropdown
import com.dominio.bloommind.ui.navigation.Routes
import com.dominio.bloommind.ui.utils.IconProvider
import com.dominio.bloommind.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    profileViewModel: ProfileViewModel,
    navController: NavController,
    newIconId: String?
) {
    var name by remember(userProfile.name) { mutableStateOf(userProfile.name) }
    var email by remember(userProfile.email) { mutableStateOf(userProfile.email) } // CORRECTED
    var birthDate by remember(userProfile.birthDate) { mutableStateOf(userProfile.birthDate) }
    var gender by remember(userProfile.gender) { mutableStateOf(userProfile.gender) }
    var iconId by remember(userProfile.iconId) { mutableStateOf(userProfile.iconId) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(newIconId) {
        if (newIconId != null) iconId = newIconId
    }

    val hasChanges = name != userProfile.name ||
            email != userProfile.email ||
            birthDate != userProfile.birthDate ||
            gender != userProfile.gender ||
            iconId != userProfile.iconId

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(id = R.string.profile_delete_title)) },
            text = { Text(stringResource(id = R.string.profile_delete_confirmation)) },
            confirmButton = {
                Button(
                    onClick = {
                        profileViewModel.deleteProfile()
                        showDeleteDialog = false
                        navController.navigate(Routes.AUTH_GRAPH) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(id = R.string.accept_button))
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(id = R.string.cancel_button))
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            if (hasChanges) {
                FloatingActionButton(
                    onClick = {
                        profileViewModel.updateProfile(name, email, birthDate, gender, iconId)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Save, contentDescription = stringResource(id = R.string.save_changes_desc))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.profile_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ProfileImage(
                iconId = iconId,
                onEditClick = { navController.navigate(Routes.ICON_SELECTION_FROM_PROFILE) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            EditableProfileField(label = stringResource(id = R.string.label_name), value = name, onValueChange = { name = it })
            EditableProfileField(label = stringResource(id = R.string.label_email), value = email, onValueChange = { email = it })

            DatePickerField(label = stringResource(id = R.string.label_birthdate), selectedDate = birthDate, onDateSelected = { birthDate = it })

            Spacer(modifier = Modifier.height(24.dp))

            GenderDropdown(selectedGender = gender, onGenderSelected = { gender = it })

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.delete_account_label),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = LocalIndication.current,
                        onClick = { showDeleteDialog = true }
                    )
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun ProfileImage(iconId: String, onEditClick: () -> Unit) {
    val resourceId = IconProvider.getIconResource(iconId)

    Box(contentAlignment = Alignment.BottomEnd) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = stringResource(id = R.string.profile_icon_desc, iconId),
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Editar foto", // This one is still hardcoded
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    onClick = onEditClick
                )
                .padding(6.dp)
        )
    }
}

@Composable
private fun EditableProfileField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        singleLine = true
    )
}
