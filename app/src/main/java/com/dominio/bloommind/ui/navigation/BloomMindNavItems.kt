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
