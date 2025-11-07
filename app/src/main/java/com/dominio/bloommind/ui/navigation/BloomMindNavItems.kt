package com.dominio.bloommind.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.dominio.bloommind.R

sealed class BloomMindNavItems(
    val route: String,
    val displayNameRes: Int,
    val icon: ImageVector
) {
    object Home : BloomMindNavItems("home", R.string.nav_home, Icons.Filled.Home)
    object Chat : BloomMindNavItems("chat", R.string.nav_chat, Icons.AutoMirrored.Filled.Chat)
    object Profile : BloomMindNavItems("profile", R.string.nav_profile, Icons.Filled.AccountCircle)
}

object Routes {
    const val AUTH_GRAPH = "auth_graph"
    const val MAIN_GRAPH = "main_graph"
    const val CHECK_IN_GRAPH = "check_in_graph"

    const val ICON_SELECTION = "icon_selection"
    const val SIGN_UP = "signup/{iconId}"
    fun createSignUpRoute(iconId: String) = "signup/$iconId"

    const val ICON_SELECTION_FROM_PROFILE = "icon_selection_from_profile"

    const val CHECK_IN = "check_in"
    const val BAD_EMOTIONS = "bad_emotions"
    const val OKAY_EMOTIONS = "okay_emotions"
    const val GOOD_EMOTIONS = "good_emotions"

    const val AFFIRMATION = "affirmation/{affirmationText}/{imageIndex}"
    fun createAffirmationRoute(affirmationText: String, imageIndex: Int) = "affirmation/$affirmationText/$imageIndex"
    const val AFFIRMATION_DETAIL = "affirmation_detail"

    const val MAIN_APP_GRAPH = "main_app_graph"
    const val CATEGORIES = "categories"
    const val CATEGORY_DETAIL = "category_detail"
}
