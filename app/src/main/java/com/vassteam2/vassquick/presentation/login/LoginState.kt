package com.vassteam2.vassquick.presentation.login

import com.vassteam2.vassquick.domain.model.PersonalizedSnackbar
import com.vassteam2.vassquick.ui.theme.Blue20
import com.vassteam2.vassquick.ui.theme.Blue80

data class LoginState(
    val login: String = "",
    val password: String = "",
    val personalizedSnackbar: PersonalizedSnackbar = PersonalizedSnackbar(
        message = null,
        contentColor = Blue80,
        containerColor = Blue20
    ),
    val successLogin: Boolean = false,
    val errorMessageLogin: String = "",
    val errorMessagePassword: String = "",
    val checkSaveUser: Boolean = false,
    var isValidLogin: Boolean = false,
    var isValidPassword: Boolean = false,
    val isLogin: Boolean = false,
)
