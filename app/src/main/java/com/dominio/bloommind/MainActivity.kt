package com.dominio.bloommind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dominio.bloommind.data.datastore.ProfileRepository
import com.dominio.bloommind.ui.components.BottomNavigationBar
import com.dominio.bloommind.ui.navigation.BloomMindNavItems
import com.dominio.bloommind.ui.navigation.Routes
import com.dominio.bloommind.ui.screens.ProfileScreen
import com.dominio.bloommind.ui.screens.SignUpScreen
import com.dominio.bloommind.ui.theme.BloomMindTheme
import com.dominio.bloommind.viewmodel.ProfileState
import com.dominio.bloommind.viewmodel.ProfileViewModel
import com.dominio.bloommind.viewmodel.ProfileViewModelFactory
import com.dominio.bloommind.ui.screens.ChatScreen

@Composable
fun PlaceholderScreen(screenName: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Pantalla: $screenName")
    }
}
class MainActivity : ComponentActivity() {
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(profileRepository)
    }
    private lateinit var profileRepository: ProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileRepository = ProfileRepository(applicationContext)

        setContent {
            BloomMindTheme {
                val navController = rememberNavController()
                val currentState by profileViewModel.profileState.collectAsStateWithLifecycle()

                val navItems = listOf(
                    BloomMindNavItems.Home,
                    BloomMindNavItems.Chat,
                    BloomMindNavItems.Profile
                )
                when (val state = currentState) {
                    is ProfileState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is ProfileState.LoggedIn, is ProfileState.NotLoggedIn -> {
                        val startDestination = if (state is ProfileState.LoggedIn) {
                            BloomMindNavItems.Home.route
                        } else {
                            Routes.SIGN_UP
                        }
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        val showScaffold = currentRoute != Routes.SIGN_UP
                        Scaffold(
                            bottomBar = {
                                if (showScaffold) {
                                    BottomNavigationBar(navController = navController, items = navItems)
                                }
                            }
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = startDestination,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable(Routes.SIGN_UP) {
                                    SignUpScreen(
                                        profileRepository = profileRepository,
                                        onSignUpComplete = {
                                            navController.navigate(BloomMindNavItems.Home.route) {
                                                popUpTo(Routes.SIGN_UP) { inclusive = true }
                                            }
                                        }
                                    )
                                }
                                // Rutas principales
                                composable(BloomMindNavItems.Home.route) { PlaceholderScreen("Home") }
                                composable(BloomMindNavItems.Chat.route) { ChatScreen() }
                                composable(BloomMindNavItems.Profile.route) {
                                    if (state is ProfileState.LoggedIn) {
                                        ProfileScreen(userProfile = state.userProfile)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}