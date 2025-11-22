package com.dominio.bloommind.ui.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dominio.bloommind.R
import java.util.*

@Composable
fun DatePickerField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Calculate Date Bounds
    // Max date: Dec 31, 2012 (approx 13 years old from 2025)
    val maxDateCalendar = Calendar.getInstance()
    maxDateCalendar.set(2012, Calendar.DECEMBER, 31)
    
    // Min date: Jan 1, 1940
    val minDateCalendar = Calendar.getInstance()
    minDateCalendar.set(1940, Calendar.JANUARY, 1)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            // Format to YYYY-MM-DD for consistency with ISO format used in ValidationUtils
            val formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    
    // Apply limits to the DatePicker
    datePickerDialog.datePicker.minDate = minDateCalendar.timeInMillis
    datePickerDialog.datePicker.maxDate = maxDateCalendar.timeInMillis

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        isError = isError,
        supportingText = { if (errorMessage != null) Text(errorMessage) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(id = R.string.date_picker_cd),
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    onClick = { datePickerDialog.show() }
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(selectedGender: String, onGenderSelected: (String) -> Unit) {
    val genderOptions = listOf(
        stringResource(id = R.string.gender_option_she),
        stringResource(id = R.string.gender_option_he),
        stringResource(id = R.string.gender_option_other)
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        OutlinedTextField(
            value = selectedGender,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(id = R.string.gender_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genderOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onGenderSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
