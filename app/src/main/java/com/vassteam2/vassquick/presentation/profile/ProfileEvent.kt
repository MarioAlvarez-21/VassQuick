package com.vassteam2.vassquick.presentation.profile

sealed class ProfileEvent {

    data class ChangeNick(val newNick: String): ProfileEvent()
    data class ChangePassword(val newPassword: String) : ProfileEvent()
    data class CheckBiometric(val checkBiometric: Boolean) : ProfileEvent()
    object ClickSaveUpdate : ProfileEvent()
    object ResetMessage : ProfileEvent()

}