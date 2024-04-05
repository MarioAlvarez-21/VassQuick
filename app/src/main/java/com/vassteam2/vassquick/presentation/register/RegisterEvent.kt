package com.vassteam2.vassquick.presentation.register

sealed class RegisterEvent {
    object ClickRegister : RegisterEvent()
    data class ChangeNick(val newNick: String): RegisterEvent()
    data class ChangeLogin(val newLogin: String): RegisterEvent()
    data class ChangePassword(val newPassword: String) : RegisterEvent()
    data class ChangeRepeatPassword(val newRepeatPassword: String) : RegisterEvent()
    object ResetMessage : RegisterEvent()
}