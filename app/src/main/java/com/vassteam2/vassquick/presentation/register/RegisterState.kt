package com.vassteam2.vassquick.presentation.register

import com.vassteam2.vassquick.domain.model.PersonalizedSnackbar
import com.vassteam2.vassquick.ui.theme.Blue20
import com.vassteam2.vassquick.ui.theme.Blue80

data class RegisterState(
    val nick: String = "",
    val login: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val messageNick: String = "",
    val messageLogin: String = "",
    val messagePassword: String = "",
    val messageRepeatPassword: String = "",
    val isValidNick: Boolean = false,
    val isValidLogin: Boolean = false,
    val isValidPassword: Boolean = false,
    val isValidRepeatPassword: Boolean = false,
    val messageSuccessOrError: String = "",
    val personalizedSnackbar: PersonalizedSnackbar = PersonalizedSnackbar(
        message = null,
        contentColor = Blue80,
        containerColor = Blue20
    ),
    val successRegister: Boolean = false
)