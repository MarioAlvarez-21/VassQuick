package com.vassteam2.vassquick.domain.use_case

import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.Chat
import com.vassteam2.vassquick.domain.repository.VassQuickRepository

class GetChatByIdUseCase(
    private val repository: VassQuickRepository
) {

    suspend operator fun invoke(token: String, idChat: String): ApiResponse<Chat> {
        return repository.getChatById(token = token, idChat = idChat)
    }
}