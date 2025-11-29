package com.dominio.bloommind.ui.navigation

object Routes {
    const val WELCOME = "welcome"
    const val ICON_SELECTION = "icon_selection"
    const val SIGN_UP = "sign_up/{iconId}"
    const val AUTH_GRAPH = "auth_graph"
    const val MAIN_GRAPH = "main_graph"
    const val CHECK_IN_GRAPH = "check_in_graph"
    const val CHECK_IN = "check_in"
    const val BAD_EMOTIONS = "bad_emotions"
    const val OKAY_EMOTIONS = "okay_emotions"
    const val GOOD_EMOTIONS = "good_emotions"
    const val AFFIRMATION = "affirmation/{affirmationText}/{imageIndex}"
    const val AFFIRMATION_BASE = "affirmation"
    const val ICON_SELECTION_FROM_PROFILE = "icon_selection_from_profile"
    const val EMOTIONS_HISTORY = "emotions_history"

    fun createSignUpRoute(iconId: String) = "sign_up/$iconId"
}
