package com.vassteam2.vassquick.domain.use_case

import androidx.paging.PagingData
import com.vassteam2.vassquick.domain.model.Message
import com.vassteam2.vassquick.domain.repository.VassQuickRepository
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(
    private val repository: VassQuickRepository
) {
    operator fun invoke(
        token: String,
        idChat: String,
        pageSize: Int = 8,
        prefetchDistance: Int = 6
    ): Flow<PagingData<Message>> {
        return repository.getMessageStream(
            token = token,
            idChat = idChat,
            pageSize = pageSize,
            prefetchDistance = prefetchDistance
        )
    }
}