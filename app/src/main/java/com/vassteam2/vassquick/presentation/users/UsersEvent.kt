package com.vassteam2.vassquick.presentation.users

import com.vassteam2.vassquick.domain.model.User

sealed class UsersEvent {
    data class ClickUser(val user: User) : UsersEvent()
    data class ResetLoading(val newReset: UsersState): UsersEvent()
}