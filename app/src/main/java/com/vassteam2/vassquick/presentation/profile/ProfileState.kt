package com.vassteam2.vassquick.presentation.profile

import com.vassteam2.vassquick.domain.model.PersonalizedSnackbar
import com.vassteam2.vassquick.domain.model.User
import com.vassteam2.vassquick.ui.theme.Blue20
import com.vassteam2.vassquick.ui.theme.Blue80

data class ProfileState(
    val user: User? = null,
    val messageNick: String = "",
    val messagePassword: String = "",
    val invalideNick: Boolean = false,
    val invalidePassword: Boolean = false,
    val personalizedSnackbar: PersonalizedSnackbar = PersonalizedSnackbar(
        message = null,
        contentColor = Blue80,
        containerColor = Blue20
    ),
    val successUpdate: Boolean = false,
    val checkBiometric: Boolean = false,
    val userCopy: User? = null
)