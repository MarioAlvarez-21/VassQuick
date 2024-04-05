import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.Message
import com.vassteam2.vassquick.domain.repository.VassQuickRepository

class GetLastMessageUseCase(private val repository: VassQuickRepository) {
    suspend operator fun invoke(token: String, chatId: String): Message? {
        return when (val response = repository.getLastMessage(token, chatId)) {
            is ApiResponse.Success -> response.data
            is ApiResponse.Error -> null
        }
    }
}

