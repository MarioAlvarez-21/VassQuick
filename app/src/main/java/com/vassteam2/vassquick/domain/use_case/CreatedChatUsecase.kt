package com.vassteam2.vassquick.domain.use_case

import com.vassteam2.vassquick.data.request.ChatRequest
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.CreatedChat
import com.vassteam2.vassquick.domain.repository.VassQuickRepository

class CreatedChatUsecase(val repository: VassQuickRepository) {
    suspend operator fun invoke(source: String, target: String, token: String): ApiResponse<CreatedChat> {
        return repository.newChat(chatRequest = ChatRequest(source = source, target = target), token = token)
    }
}