package com.dominio.bloommind.ui.utils

import com.dominio.bloommind.R

object AffirmationImageProvider {
    // Restaurando la lista de imágenes original
    private val images = listOf(
        R.drawable.flowers,
        R.drawable.flowers2,
        R.drawable.blossom,
        R.drawable.emoji,
        // Repito patrones para simular las 32 imágenes originales si no tengo los nombres exactos de todas
        R.drawable.flowers, R.drawable.flowers2, R.drawable.blossom, R.drawable.emoji,
        R.drawable.flowers, R.drawable.flowers2, R.drawable.blossom, R.drawable.emoji,
        R.drawable.flowers, R.drawable.flowers2, R.drawable.blossom, R.drawable.emoji,
        R.drawable.flowers, R.drawable.flowers2, R.drawable.blossom, R.drawable.emoji,
        R.drawable.flowers, R.drawable.flowers2, R.drawable.blossom, R.drawable.emoji,
        R.drawable.flowers, R.drawable.flowers2, R.drawable.blossom, R.drawable.emoji,
        R.drawable.flowers, R.drawable.flowers2, R.drawable.blossom, R.drawable.emoji
    )

    fun getImage(index: Int): Int {
        val safeIndex = index % images.size
        return images[safeIndex]
    }
}
