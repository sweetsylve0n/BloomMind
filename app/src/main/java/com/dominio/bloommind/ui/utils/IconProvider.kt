package com.dominio.bloommind.ui.utils

import androidx.annotation.DrawableRes
import com.dominio.bloommind.R

object IconProvider {
    val iconList = listOf(
        "icon1", "icon2", "icon3", "icon4", "icon5", "icon6", "icon7", "icon8"
    )

    private val iconMap = mapOf(
        "icon1" to R.drawable.icon1,
        "icon2" to R.drawable.icon2,
        "icon3" to R.drawable.icon3,
        "icon4" to R.drawable.icon4,
        "icon5" to R.drawable.icon5,
        "icon6" to R.drawable.icon6,
        "icon7" to R.drawable.icon7,
        "icon8" to R.drawable.icon8
    )

    @DrawableRes
    fun getIconResource(iconId: String): Int {
        return iconMap[iconId] ?: R.drawable.ic_launcher_foreground
    }
}
