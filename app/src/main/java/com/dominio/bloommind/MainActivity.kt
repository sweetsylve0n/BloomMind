package com.dominio.bloommind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(profileRepository, applicationContext)
    }
    private lateinit var profileRepository: ProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileRepository = ProfileRepository(applicationContext)

        setContent {
            BloomMindTheme {
                val navController = rememberNavController()
                val currentState by profileViewModel.profileState.collectAsStateWithLifecycle()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val baseRoute = currentRoute?.substringBefore("?")

                val showBottomBar = baseRoute == BloomMindNavItems.Home.route || baseRoute == BloomMindNavItems.Profile.route

                val routesWithTopBar = listOf(
                    Routes.CHECK_IN,
                    Routes.BAD_EMOTIONS,
                    Routes.OKAY_EMOTIONS,
                    Routes.GOOD_EMOTIONS,
                    Routes.EMOTIONS_HISTORY
                )
                val isChatScreen = baseRoute == BloomMindNavItems.Chat.route
                val showTopBar = isChatScreen || routesWithTopBar.contains(baseRoute) || (baseRoute?.startsWith(Routes.AFFIRMATION_BASE) == true)

                Scaffold(
                    topBar = {
                        if (showTopBar) {
                            TopAppBar(
                                title = { Text(stringResource(id = R.string.go_back_home)) },
                                navigationIcon = {
                                    IconButton(onClick = { navigateToHome(navController) }) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back to Home")
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (showBottomBar) {
                            val navItems = listOf(BloomMindNavItems.Home, BloomMindNavItems.Chat, BloomMindNavItems.Profile)
                            BottomNavigationBar(navController = navController, navItems)
                        }
                    }
                ) { innerPadding ->
                    when (val state = currentState) {
                        is ProfileState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }

                        is ProfileState.LoggedIn, is ProfileState.NotLoggedIn -> {
                            val startDestination = if (state is ProfileState.LoggedIn) Routes.MAIN_GRAPH else Routes.AUTH_GRAPH

                            NavHost(
                                navController = navController,
                                startDestination = startDestination,
                                modifier = Modifier.padding(innerPadding),
                                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) },
                                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)) },
                                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) },
                                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)) }
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
                                    composable(BloomMindNavItems.Home.route) {
                                        if (state is ProfileState.LoggedIn) {
                                            HomeScreen(navController = navController, userProfile = state.userProfile)
                                        }
                                    }
                                    composable(
                                        route = BloomMindNavItems.Chat.route + "?emotions={emotions}",
                                        arguments = listOf(navArgument("emotions") {
                                            type = NavType.StringType
                                            nullable = true
                                            defaultValue = null
                                        })
                                    ) { backStackEntry ->
                                        val emotions = backStackEntry.arguments?.getString("emotions")
                                        ChatScreen(emotions = emotions)
                                    }

                                    composable(BloomMindNavItems.Profile.route) { navBackStackEntry ->
                                        val newIconId = navBackStackEntry.savedStateHandle.get<String>("newIconId")
                                        if (state is ProfileState.LoggedIn) {
                                            ProfileScreen(
                                                userProfile = state.userProfile,
                                                profileViewModel = profileViewModel,
                                                navController = navController,
                                                newIconId = newIconId
                                            )
                                        }
                                    }

                                    composable(Routes.ICON_SELECTION_FROM_PROFILE) {
                                        IconSelectionScreen(onIconSelected = {
                                            navController.previousBackStackEntry?.savedStateHandle?.set("newIconId", it)
                                            navController.popBackStack()
                                        })
                                    }

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

                                    composable(Routes.EMOTIONS_HISTORY) {
                                        EmotionsHistoryScreen(navController = navController)
                                    }
                                }

                                navigation(startDestination = Routes.CHECK_IN, route = Routes.CHECK_IN_GRAPH) {
                                    composable(Routes.CHECK_IN) { CheckInScreen(navController) }
                                    composable(Routes.BAD_EMOTIONS) { BadEmotionsScreen(navController) }
                                    composable(Routes.OKAY_EMOTIONS) { OkayEmotionsScreen(navController) }
                                    composable(Routes.GOOD_EMOTIONS) { GoodEmotionsScreen(navController) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToHome(navController: NavController) {
        navController.navigate(BloomMindNavItems.Home.route) {
            popUpTo(BloomMindNavItems.Home.route) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
}
