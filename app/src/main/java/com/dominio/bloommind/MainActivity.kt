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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.dominio.bloommind.data.datastore.ProfileRepository
import com.dominio.bloommind.ui.components.BottomNavigationBar
import com.dominio.bloommind.ui.navigation.BloomMindNavItems
import com.dominio.bloommind.ui.navigation.Routes
import com.dominio.bloommind.ui.screens.*
import com.dominio.bloommind.ui.theme.BloomMindTheme
import com.dominio.bloommind.viewmodel.ProfileState
import com.dominio.bloommind.viewmodel.ProfileViewModel
import com.dominio.bloommind.viewmodel.ProfileViewModelFactory
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
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
                            Routes.MAIN_GRAPH
                        } else {
                            Routes.AUTH_GRAPH
                        }

                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        val showScaffold = currentRoute != null &&
                                currentRoute != Routes.AUTH_GRAPH &&
                                currentRoute != Routes.ICON_SELECTION &&
                                !currentRoute.startsWith("signup/")

                        Scaffold(
                            bottomBar = {
                                if (showScaffold) {
                                    BottomNavigationBar(navController = navController, navItems)
                                }
                            }
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = startDestination,
                                modifier = Modifier.padding(innerPadding)
                            ) {

                                navigation(startDestination = Routes.ICON_SELECTION, route = Routes.AUTH_GRAPH) {
                                    composable(Routes.ICON_SELECTION) {
                                        IconSelectionScreen(onIconSelected = { navController.navigate(Routes.createSignUpRoute(it)) })
                                    }
                                    composable(
                                        route = Routes.SIGN_UP,
                                        arguments = listOf(navArgument("iconId") { type = NavType.StringType })
                                    ) { backStackEntry ->
                                        val iconId = backStackEntry.arguments?.getString("iconId") ?: "icon1"
                                        SignUpScreen(
                                            profileRepository = profileRepository,
                                            iconId = iconId,
                                            onSignUpComplete = { navController.navigate(Routes.MAIN_GRAPH) { popUpTo(Routes.AUTH_GRAPH) { inclusive = true } } }
                                        )
                                    }
                                }

                                navigation(startDestination = BloomMindNavItems.Home.route, route = Routes.MAIN_GRAPH) {
                                    composable(BloomMindNavItems.Home.route) { HomeScreen(navController = navController) }
                                    composable(BloomMindNavItems.Chat.route) { ChatScreen() }

                                    composable(BloomMindNavItems.Profile.route) { navBackStackEntry ->
                                        val newIconId = navBackStackEntry.savedStateHandle.get<String>("newIconId")
                                        if (state is ProfileState.LoggedIn) {
                                            ProfileScreen(
                                                userProfile = state.userProfile,
                                                profileViewModel = profileViewModel,
                                                navController = navController,
                                                newIconId = newIconId
                                            )
                                            navBackStackEntry.savedStateHandle.remove<String>("newIconId")
                                        }
                                    }

                                    composable(Routes.ICON_SELECTION_FROM_PROFILE) {
                                        IconSelectionScreen(onIconSelected = {
                                            navController.previousBackStackEntry?.savedStateHandle?.set("newIconId", it)
                                            navController.popBackStack()
                                        })
                                    }

                                    composable(Routes.CHECK_IN) { CheckInScreen() }
                                    composable(Routes.AFFIRMATION_DETAIL) { AffirmationDetailScreen() }
                                    composable(
                                        route = Routes.AFFIRMATION,
                                        arguments = listOf(
                                            navArgument("affirmationText") { type = NavType.StringType },
                                            navArgument("imageIndex") { type = NavType.IntType }
                                        )
                                    ) { backStackEntry ->
                                        val text = backStackEntry.arguments?.getString("affirmationText") ?: ""
                                        val imageIndex = backStackEntry.arguments?.getInt("imageIndex") ?: 0
                                        val decodedText = URLDecoder.decode(text, StandardCharsets.UTF_8.name())
                                        AffirmationScreen(affirmationText = decodedText, imageIndex = imageIndex)
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