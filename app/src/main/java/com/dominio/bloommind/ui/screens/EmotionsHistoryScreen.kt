package com.dominio.bloommind.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dominio.bloommind.R
import com.dominio.bloommind.viewmodel.CheckInViewModel
import com.dominio.bloommind.viewmodel.HomeViewModel
import com.dominio.bloommind.viewmodel.HomeViewModelFactory
import com.dominio.bloommind.viewmodel.LastCheckinsUiState

@Composable
fun EmotionsHistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))
    val lastState by homeViewModel.lastCheckinsState.collectAsState()
    
    // We use CheckInViewModel to get the emotion definitions (Good, Okay, Bad)
    val checkInViewModel: CheckInViewModel = viewModel()
    val badEmotionsIds = checkInViewModel.badEmotions.map { it.nameResId }.toSet()
    val okayEmotionsIds = checkInViewModel.okayEmotions.map { it.nameResId }.toSet()
    val goodEmotionsIds = checkInViewModel.goodEmotions.map { it.nameResId }.toSet()

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
                // s.entries is List<Pair<String, Set<Int>>>
                // We need to invert it to show chronologically (oldest to newest) for a graph,
                // or keep it descending (newest first) depending on preference. 
                // Usually charts are left-to-right (old to new).
                val chartData = s.entries.sortedBy { it.first } 

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    Text(
                        text = stringResource(id = R.string.emotions_history_title),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (chartData.isEmpty()) {
                         Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = stringResource(id = R.string.todays_emotions_card_prompt))
                        }
                    } else {
                        Text(
                            text = "Last 5 Check-ins Analysis",
                            style = MaterialTheme.typography.titleMedium
                        )

                        // Graph Container
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                chartData.forEach { (date, emotionsSet) ->
                                    // Calculate counts per category
                                    val goodCount = emotionsSet.count { it in goodEmotionsIds }
                                    val okayCount = emotionsSet.count { it in okayEmotionsIds }
                                    val badCount = emotionsSet.count { it in badEmotionsIds }
                                    val total = goodCount + okayCount + badCount
                                    
                                    // Just a safe guard for div by zero, though total shouldn't be 0 if check-in exists
                                    val maxPossible = 4f // Assuming max 4 emotions per check-in as per CheckIn logic
                                    
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom,
                                        modifier = Modifier.fillMaxHeight()
                                    ) {
                                        // Stacked Bar
                                        Column(
                                            modifier = Modifier
                                                .width(24.dp)
                                                .weight(1f, fill = false), // allow column to shrink/grow based on content
                                            verticalArrangement = Arrangement.Bottom
                                        ) {
                                            // We normalize height to available space. 
                                            // Or simpler: Each segment has a weight proportional to its count.
                                            // But to make bars aligned at bottom and variable height based on total emotions count:
                                            
                                            // Let's use absolute heights relative to a max height container (e.g. 150dp)
                                            val barMaxHeight = 180.dp
                                            val heightPerUnit = barMaxHeight / maxPossible

                                            if (goodCount > 0) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(heightPerUnit * goodCount)
                                                        .background(Color(0xFF81C784)) // Light Green
                                                )
                                            }
                                            if (okayCount > 0) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(heightPerUnit * okayCount)
                                                        .background(Color(0xFFFFF176)) // Yellow
                                                )
                                            }
                                            if (badCount > 0) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(heightPerUnit * badCount)
                                                        .background(Color(0xFFE57373)) // Red
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        // Date Label (e.g. 2025-10-18 -> 10-18)
                                        Text(
                                            text = date.takeLast(5), 
                                            style = MaterialTheme.typography.labelSmall,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }

                        // Legend
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            LegendItem(color = Color(0xFF81C784), text = stringResource(id = R.string.emotion_good))
                            LegendItem(color = Color(0xFFFFF176), text = stringResource(id = R.string.emotion_okay))
                            LegendItem(color = Color(0xFFE57373), text = stringResource(id = R.string.emotion_bad))
                        }
                    }
                }
            }
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
