package com.vassteam2.vassquick.domain.use_case

import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.DeleteChat
import com.vassteam2.vassquick.domain.repository.VassQuickRepository
import javax.inject.Inject

class DeleteChatUseCase@Inject constructor(private val repository: VassQuickRepository) {

    suspend operator fun invoke(id: String, token: String): ApiResponse<DeleteChat> {
        return repository.deleteChat(id, token)
    }
}