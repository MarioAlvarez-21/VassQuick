package com.vassteam2.vassquick.presentation.chat

import com.vassteam2.vassquick.domain.model.Chat

sealed class ChatEvent {

    data class OnLongItemClick(val chat: Chat) : ChatEvent()
    data class ConfirmDeleteChat(val chat: Chat) : ChatEvent()
    data class SetSearchQuery(val query: String): ChatEvent()
    data class UpdateChatsOnlineStatus(val isOnline:Boolean): ChatEvent()
    object CancelDeleteChat : ChatEvent()
    object ShowLogoutConfirmation : ChatEvent()
    object ConfirmLogout : ChatEvent()
    object DismissLogoutConfirmation: ChatEvent()
    object ClearSearchAndReloadChats: ChatEvent()
    data class ResetLoading(val newReset: ChatState): ChatEvent()

    object Refresh : ChatEvent()
}
