package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dominio.bloommind.data.datastore.UserProfile
@Composable
fun ProfileScreen(userProfile: UserProfile) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Mi Perfil",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ProfileInfoCard(
            name = userProfile.name,
            email = userProfile.email,
            birthDate = userProfile.birthDate,
            gender = userProfile.gender
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* TODO: Implementar borrado */ },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Eliminar Cuenta")
        }
    }
}
@Composable
private fun ProfileInfoCard(name: String, email: String, birthDate: String, gender: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ProfileInfoRow(label = "Nombre", value = name)
            ProfileInfoRow(label = "Email", value = email)
            ProfileInfoRow(label = "Fecha de Nacimiento", value = birthDate)
            ProfileInfoRow(label = "Género", value = gender)
        }
    }
}
@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}