package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.response.DeleteChatResponse
import com.vassteam2.vassquick.domain.model.DeleteChat

fun DeleteChatResponse.toDeleteChat(): DeleteChat{
    return DeleteChat(
        success = this.success,
        message = this.message
    )
}