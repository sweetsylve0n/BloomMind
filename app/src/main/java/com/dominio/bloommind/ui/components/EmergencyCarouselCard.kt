package com.dominio.bloommind.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dominio.bloommind.R

data class EmergencyContact(
    val organization: String,
    val phone: String,
    val schedule: String,
    val description: String
)

val emergencyContacts = listOf(
    EmergencyContact(
        organization = "Línea Aquí Estoy",
        phone = "800-273-7865",
        schedule = "L-V: 2pm-10pm, S: 9am-4pm",
        description = "Servicio gratuito para atención de urgencias psicológicas y emocionales."
    ),
    EmergencyContact(
        organization = "Despacho de Apoyo Psicológico (DAP)",
        phone = "9-1-1",
        schedule = "24 horas del día",
        description = "Ofrece apoyo emocional sin costo."
    ),
    EmergencyContact(
        organization = "Colegio de Profesionales en Psicología",
        phone = "2272-3774",
        schedule = "Lunes a Viernes",
        description = "Atención de urgencias psicológicas y emocionales."
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmergencyCarouselCard() {
    val pagerState = rememberPagerState(pageCount = { emergencyContacts.size })

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.emergency_carousel_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 16.dp
            ) { page ->
                val contact = emergencyContacts[page]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = contact.organization, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                    Text(text = contact.phone, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 2.dp))
                    Text(text = contact.schedule, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 4.dp))
                    Text(text = contact.description, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Indicator dots
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }
    }
}
