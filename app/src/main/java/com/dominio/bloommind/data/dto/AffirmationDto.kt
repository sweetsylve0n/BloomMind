package com.dominio.bloommind.data.dto

import com.google.gson.annotations.SerializedName

data class AffirmationDto(
    @SerializedName("text") val text: String,
    @Transient var imageIndex: Int = 0
)
