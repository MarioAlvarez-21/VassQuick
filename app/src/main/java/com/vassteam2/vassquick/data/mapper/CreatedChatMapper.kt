package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.response.CreatedChatResponse
import com.vassteam2.vassquick.domain.model.Chat
import com.vassteam2.vassquick.domain.model.CreatedChat

fun CreatedChatResponse.toCreatedChat(): CreatedChat {
    return CreatedChat(
        success = this.success,
        created = this.created,
        chat = this.chat.toChat()
    )
}