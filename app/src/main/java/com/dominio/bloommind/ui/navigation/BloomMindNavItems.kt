package com.dominio.bloommind.ui.navigation

import com.dominio.bloommind.R

sealed class BloomMindNavItems(
    val route: String,
    val displayNameRes: Int,
    val iconRes: Int
) {
    object Home : BloomMindNavItems("home", R.string.nav_home, R.drawable.home_icon)
    object Chat : BloomMindNavItems("chat", R.string.nav_chat, R.drawable.chat_icon)
    object Profile : BloomMindNavItems("profile", R.string.nav_profile, R.drawable.profile_icon)
}
