package com.dominio.bloommind.ui.utils

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isDateInThePast(dateStr: String): Boolean {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    sdf.isLenient = false
    return try {
        val date = sdf.parse(dateStr)
        date?.before(Date()) ?: false
    } catch (e: Exception) {
        false
    }
}
