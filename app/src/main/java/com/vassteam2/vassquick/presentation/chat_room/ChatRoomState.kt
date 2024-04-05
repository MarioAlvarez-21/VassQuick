package com.vassteam2.vassquick.presentation.chat_room

import com.vassteam2.vassquick.domain.model.Chat
import com.vassteam2.vassquick.domain.model.PersonalizedSnackbar
import com.vassteam2.vassquick.domain.model.User
import com.vassteam2.vassquick.ui.theme.Blue20
import com.vassteam2.vassquick.ui.theme.Blue80

data class ChatRoomState(
    val chat: Chat? = null,
    val receivingUser: User? = null,
    val newMessage: String = "",
    val isNewMessage: Boolean = false,
    val personalizedSnackbar: PersonalizedSnackbar = PersonalizedSnackbar(
        message = null,
        contentColor = Blue80,
        containerColor = Blue20
    ),
)
