package com.vassteam2.vassquick.domain.use_case

import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.NewMessage
import com.vassteam2.vassquick.domain.repository.VassQuickRepository

class PostNewMessageUseCase(
    private val repository: VassQuickRepository
) {

    suspend operator fun invoke(token: String, newMessage: NewMessage): ApiResponse<Boolean> {
        return repository.postNewMessage(token = token, newMessage = newMessage)
    }
}