package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.request.NewMessageRequest
import com.vassteam2.vassquick.domain.model.NewMessage

fun NewMessage.toNewMessageRequest(): NewMessageRequest {
    return NewMessageRequest(
        chat = chat,
        message = message,
        source = source
    )
}