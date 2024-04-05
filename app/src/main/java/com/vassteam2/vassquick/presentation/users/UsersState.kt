package com.vassteam2.vassquick.presentation.users

import com.vassteam2.vassquick.domain.model.User

data class UsersState(
    val idChat: String = "",
    val isLoading: Boolean = false,
    val users: List<User> = emptyList()
)