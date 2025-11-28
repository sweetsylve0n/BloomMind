package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dominio.bloommind.R
import com.dominio.bloommind.ui.utils.EmotionContentProvider
import com.dominio.bloommind.viewmodel.CheckInViewModel
import com.dominio.bloommind.viewmodel.HomeViewModel
import com.dominio.bloommind.viewmodel.HomeViewModelFactory
import com.dominio.bloommind.viewmodel.LastCheckinsUiState
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EmotionsHistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))
    val lastState by homeViewModel.lastCheckinsState.collectAsState()
    
    val checkInViewModel: CheckInViewModel = viewModel()
    val badEmotionsIds = checkInViewModel.badEmotions.map { it.nameResId }.toSet()
    val okayEmotionsIds = checkInViewModel.okayEmotions.map { it.nameResId }.toSet()
    val goodEmotionsIds = checkInViewModel.goodEmotions.map { it.nameResId }.toSet()

    val goodColor = Color(0xFFFCD756)
    val okayColor = Color(0xFF74D7FB)
    val badColor = Color(0xFF680F1D)

    LaunchedEffect(Unit) {
        homeViewModel.fetchLastCheckins(5)
    }

    Scaffold { padding ->
        when (val s = lastState) {
            is LastCheckinsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LastCheckinsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = s.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is LastCheckinsUiState.Success -> {
                val chartData = s.entries.sortedBy { it.first }
                val latestCheckIn = chartData.lastOrNull()
                val latestEmotions = latestCheckIn?.second ?: emptySet()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    item {
                        Text(
                            text = stringResource(id = R.string.emotions_history_title),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (latestEmotions.isNotEmpty()) {
                        items(latestEmotions.toList()) { emotionId ->
                            EmotionDetailCard(emotionId = emotionId)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    } else {
                        item {
                             Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                                Text(text = stringResource(id = R.string.todays_emotions_card_prompt))
                            }
                        }
                    }

                    if (chartData.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(id = R.string.recent_emotions_title),
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp)
                            )
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        chartData.forEach { (dateString, emotionsSet) ->
                                            val goodCount = emotionsSet.count { it in goodEmotionsIds }
                                            val okayCount = emotionsSet.count { it in okayEmotionsIds }
                                            val badCount = emotionsSet.count { it in badEmotionsIds }
                                            val totalCount = goodCount + okayCount + badCount
                                            
                                            var dayStr = ""
                                            var timeStr = ""
                                            try {
                                                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                                val dateObj = inputFormat.parse(dateString)
                                                if (dateObj != null) {
                                                    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
                                                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                                                    dayStr = dayFormat.format(dateObj).uppercase()
                                                    timeStr = timeFormat.format(dateObj)
                                                } else {
                                                    dayStr = dateString.takeLast(5)
                                                }
                                            } catch (_: Exception) {
                                                dayStr = "?"
                                            }

                                            val maxPossible = 4f
                                            
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Bottom,
                                                modifier = Modifier.fillMaxHeight()
                                            ) {
                                                Text(
                                                    text = totalCount.toString(),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                
                                                Spacer(modifier = Modifier.height(4.dp))

                                                Column(
                                                    modifier = Modifier
                                                        .width(32.dp)
                                                        .weight(1f, fill = false),
                                                    verticalArrangement = Arrangement.Bottom
                                                ) {
                                                    val barMaxHeight = 220.dp
                                                    val heightPerUnit = barMaxHeight / maxPossible

                                                    if (goodCount > 0) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(heightPerUnit * goodCount)
                                                                .background(goodColor)
                                                        )
                                                    }
                                                    if (okayCount > 0) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(heightPerUnit * okayCount)
                                                                .background(okayColor) 
                                                        )
                                                    }
                                                    if (badCount > 0) {
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .height(heightPerUnit * badCount)
                                                                .background(badColor) 
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))
                                                
                                                Text(
                                                    text = dayStr, 
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = timeStr, 
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.Gray,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                LegendItem(color = goodColor, text = stringResource(id = R.string.emotion_good))
                                Spacer(modifier = Modifier.width(16.dp))
                                LegendItem(color = okayColor, text = stringResource(id = R.string.emotion_okay))
                                Spacer(modifier = Modifier.width(16.dp))
                                LegendItem(color = badColor, text = stringResource(id = R.string.emotion_bad))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmotionDetailCard(emotionId: Int) {
    val detail = EmotionContentProvider.getEmotionDetail(emotionId)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = detail.nameResId),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = detail.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Tip: ${detail.advice}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Image(
                painter = painterResource(id = detail.imageResId),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall)
    }
}