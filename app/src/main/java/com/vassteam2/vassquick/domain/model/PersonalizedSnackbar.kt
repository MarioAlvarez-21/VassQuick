package com.vassteam2.vassquick.domain.model

data class PersonalizedSnackbar(
    var message: String?,
    val contentColor: androidx.compose.ui.graphics.Color,
    val containerColor: androidx.compose.ui.graphics.Color
)
