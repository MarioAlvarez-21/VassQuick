package com.vassteam2.vassquick.presentation.chat_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.di.UserSingleton
import com.vassteam2.vassquick.domain.model.Chat
import com.vassteam2.vassquick.domain.model.Message
import com.vassteam2.vassquick.domain.model.NewMessage
import com.vassteam2.vassquick.domain.model.User
import com.vassteam2.vassquick.domain.use_case.VassQuickUseCases
import com.vassteam2.vassquick.ui.theme.Red40
import com.vassteam2.vassquick.ui.theme.Red90
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val vassQuickUseCases: VassQuickUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(ChatRoomState())
    val state = _state.asStateFlow()

    var messagesFlow: Flow<PagingData<Message>>? = null

    fun start(idChat: String) {
        val token = UserSingleton.token
        messagesFlow = vassQuickUseCases.getMessagesUseCase(token = token, idChat = idChat)
            .cachedIn(viewModelScope)

        var receivingUser: User? = null
        if (token.isNotEmpty()) {
            viewModelScope.launch {
                var chat = getChat(token = token, idChat = idChat)
                if (chat != null) {
                    val receivingUserId =
                        if (chat.source != UserSingleton.user.id) chat.source else chat.target
                    receivingUser =
                        getReceivingUserId(token = token, idUserReceiving = receivingUserId)
                }
                _state.update {
                    it.copy(
                        chat = chat,
                        receivingUser = receivingUser
                    )
                }
            }
        }
    }

    fun onEvent(event: ChatRoomEvent) {
        when (event) {
            is ChatRoomEvent.SendMessage -> {
                if (state.value.newMessage.isNotEmpty()) {
                    sendMessage()
                }
            }

            is ChatRoomEvent.UpdateIsNewMessage -> {
                _state.update {
                    it.copy(
                        isNewMessage = false
                    )
                }
            }

            is ChatRoomEvent.ChangeMessage -> {
                _state.update {
                    it.copy(
                        newMessage = event.newMessage
                    )
                }
            }

            is ChatRoomEvent.ResetMessage -> {
                _state.update {
                    it.copy(
                        personalizedSnackbar = state.value.personalizedSnackbar.copy(message = null)
                    )
                }
            }
        }
    }

    private fun sendMessage() {
        val token = UserSingleton.token
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                token.let {
                    vassQuickUseCases.postNewMessageUseCase(
                        token = it, newMessage = NewMessage(
                            chat = state.value.chat!!.id,
                            source = UserSingleton.user.id,
                            message = state.value.newMessage.trim()
                        )
                    )
                }
            when (response) {
                is ApiResponse.Success -> {
                    _state.update {
                        it.copy(
                            isNewMessage = true,
                            newMessage = ""
                        )
                    }
                }

                is ApiResponse.Error -> {
                    _state.update {
                        it.copy(
                            personalizedSnackbar = state.value.personalizedSnackbar.copy
                                (
                                message = response.message,
                                containerColor = Red90,
                                contentColor = Red40
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun getChat(token: String, idChat: String): Chat? {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            when (val response =
                vassQuickUseCases.getChatByIdUseCase(token = token, idChat = idChat)) {
                is ApiResponse.Success -> response.data
                is ApiResponse.Error -> {
                    _state.update {
                        it.copy(
                            personalizedSnackbar = state.value.personalizedSnackbar.copy
                                (
                                message = response.message,
                                containerColor = Red90,
                                contentColor = Red40
                            )
                        )
                    }
                    null
                }
            }
        }
    }

    private suspend fun getReceivingUserId(token: String, idUserReceiving: String): User? {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            when (val response =
                vassQuickUseCases.getUserByIdUseCase(token = token, idUser = idUserReceiving)) {
                is ApiResponse.Success -> response.data
                is ApiResponse.Error -> {
                    _state.update {
                        it.copy(
                            personalizedSnackbar = state.value.personalizedSnackbar.copy
                                (
                                message = response.message,
                                containerColor = Red90,
                                contentColor = Red40
                            )
                        )
                    }
                    null
                }
            }
        }
    }

}