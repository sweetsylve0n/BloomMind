package com.dominio.bloommind.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
sealed class BloomMindNavItems(
    val route: String,
    val displayName: String,
    val icon: ImageVector
) {
    object Home : BloomMindNavItems("home", "Home", Icons.Filled.Home)
    object Chat : BloomMindNavItems("chat", "Chat", Icons.Filled.Chat)
    object Profile : BloomMindNavItems("profile", "Profile", Icons.Filled.AccountCircle)
}
object Routes {
    const val SIGN_UP = "signup"
    const val MAIN_APP_GRAPH = "main_app_graph"
    const val CATEGORIES = "categories"
    const val CATEGORY_DETAIL = "category_detail"
    const val AFFIRMATION = "affirmation"
}