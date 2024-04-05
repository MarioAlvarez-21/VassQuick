package com.vassteam2.vassquick.presentation.chat_room

sealed class ChatRoomEvent {
    object SendMessage : ChatRoomEvent()
    object UpdateIsNewMessage : ChatRoomEvent()
    data class ChangeMessage(val newMessage: String) : ChatRoomEvent()
    object ResetMessage : ChatRoomEvent()
}
