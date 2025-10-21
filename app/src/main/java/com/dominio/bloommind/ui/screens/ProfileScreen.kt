package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dominio.bloommind.data.datastore.UserProfile
import com.dominio.bloommind.ui.utils.IconProvider

@Composable
fun ProfileScreen(userProfile: UserProfile) {
    var name by remember { mutableStateOf(userProfile.name) }
    var email by remember { mutableStateOf(userProfile.email) }
    var birthDate by remember { mutableStateOf(userProfile.birthDate) }
    var gender by remember { mutableStateOf(userProfile.gender) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Perfil",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ProfileImage(iconId = userProfile.iconId)

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            EditableProfileField(label = "Nombre", value = name, onValueChange = { name = it })
            EditableProfileField(label = "Email", value = email, onValueChange = { email = it })
            EditableProfileField(label = "Fecha de Nacimiento", value = birthDate, onValueChange = { birthDate = it }, isDateField = true)
            EditableProfileField(label = "Género", value = gender, onValueChange = { gender = it })
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Eliminar Cuenta",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = { /* TODO: Implementar borrado */ }
            )
        )
    }
}

@Composable
fun ProfileImage(iconId: String) {
    val resourceId = IconProvider.getIconResource(iconId)

    Box(contentAlignment = Alignment.BottomEnd) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = { /* TODO: Implementar selección de imagen */ }
                ),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Editar foto",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(6.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableProfileField(label: String, value: String, onValueChange: (String) -> Unit, isDateField: Boolean = false) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Transparent
            ),
            readOnly = isDateField,
            trailingIcon = {
                if(isDateField) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Select Date")
                }
            }
        )
    }
}
