package com.dominio.bloommind.ui.utils

import androidx.annotation.DrawableRes
import com.dominio.bloommind.R

object IconProvider {
    private val iconMap = mapOf(
        "icon1" to R.drawable.icon1,
        "icon2" to R.drawable.icon2,
        "icon3" to R.drawable.icon3,
        "icon4" to R.drawable.icon4
    )

    @DrawableRes
    fun getIconResource(iconId: String): Int {
        // Returns a default icon if the requested one is not found, preventing crashes.
        return iconMap[iconId] ?: R.drawable.ic_launcher_foreground
    }
}