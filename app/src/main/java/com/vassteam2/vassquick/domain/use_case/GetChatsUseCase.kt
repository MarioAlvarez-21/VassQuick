package com.vassteam2.vassquick.domain.use_case

import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.Chat
import com.vassteam2.vassquick.domain.repository.VassQuickRepository

class GetChatsUseCase(val repository: VassQuickRepository) {

    suspend operator fun invoke(
        token: String
    ): ApiResponse<List<Chat>> {

        return repository.getChats(
           token = token
            )
    }
}
