package com.vassteam2.vassquick.presentation.login

sealed class LoginEvent{
    object ClickLogin : LoginEvent()
    object BiometricAuthentication : LoginEvent()
    data class ChangeLogin(val newLogin: String): LoginEvent()
    data class ChangePassword(val newPassword: String) : LoginEvent()
    object ResetMessage : LoginEvent()
    object ReminderLogin : LoginEvent()

    object LoginAndSaveToken : LoginEvent()
}
