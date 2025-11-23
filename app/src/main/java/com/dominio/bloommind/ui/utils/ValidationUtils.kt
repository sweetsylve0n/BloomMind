package com.dominio.bloommind.ui.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object ValidationUtils {
    // Regex for name: allows letters (including accents/other languages) and spaces.
    // Ensures start of the string is a letter.
    private val nameRegex = Regex("^[\\p{L}]+(?:[ ]+[\\p{L}]+)*$")

    fun isNameValid(name: String): Boolean {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return false
        if (!nameRegex.matches(trimmed)) return false

        val words = trimmed.split(Regex("\\s+"))
        for (word in words) {
            if (word.isEmpty()) return false
            val firstChar = word[0]
            if (!firstChar.isUpperCase()) return false
        }
        return true
    }

    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    
    fun isEmailValid(email: String): Boolean {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) return false
        // No spaces allowed in email
        if (trimmed.contains(' ')) return false
        return emailRegex.matches(trimmed)
    }

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun isBirthDateValid(dateString: String): Boolean {
        val trimmed = dateString.trim()
        if (trimmed.isEmpty()) return false
        val date: LocalDate = try {
            LocalDate.parse(trimmed, formatter)
        } catch (_: DateTimeParseException) {
            return false
        }

        val min = LocalDate.of(1940, 1, 1)
        val max = LocalDate.of(2012, 12, 31)
        
        return !(date.isBefore(min) || date.isAfter(max))
    }

    fun ageFromDate(dateString: String): Int? {
         return try {
            val date = LocalDate.parse(dateString, formatter)
            val now = LocalDate.now()
            var age = now.year - date.year
            if (now < date.plusYears(age.toLong())) age--
            age
        } catch (_: Exception) {
            null
        }
    }
}
