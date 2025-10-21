package com.dominio.bloommind.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
class IconSelectionViewModel : ViewModel() {

    private val _selectedIconId = MutableStateFlow<String?>("icon1") // Default to icon1
    val selectedIconId = _selectedIconId.asStateFlow()

    fun onIconSelected(iconId: String) {
        _selectedIconId.update { iconId }
    }
}