package com.dominio.bloommind.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BloomMindNavItems(
    val route: String,
    val displayName: String,
    val icon: ImageVector
) {
    object Home : BloomMindNavItems("home", "Home", Icons.Filled.Home)
    object Chat : BloomMindNavItems("chat", "Chat", Icons.AutoMirrored.Filled.Chat)
    object Profile : BloomMindNavItems("profile", "Profile", Icons.Filled.AccountCircle)
}

object Routes {
    // Sign-Up Flow
    const val AUTH_GRAPH = "auth_graph"
    const val ICON_SELECTION = "icon_selection"
    const val SIGN_UP = "signup/{iconId}"
    fun createSignUpRoute(iconId: String) = "signup/$iconId"

    // Profile edit flow
    const val ICON_SELECTION_FROM_PROFILE = "icon_selection_from_profile"

    // Other routes
    const val MAIN_APP_GRAPH = "main_app_graph"
    const val CATEGORIES = "categories"
    const val CATEGORY_DETAIL = "category_detail"
    const val AFFIRMATION = "affirmation"
}
