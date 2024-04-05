package com.vassteam2.vassquick.presentation.chat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.di.UserSingleton
import com.vassteam2.vassquick.domain.constants.HAVE_BIOMETRIC
import com.vassteam2.vassquick.domain.constants.TOKEN_KEY
import com.vassteam2.vassquick.domain.model.Chat
import com.vassteam2.vassquick.domain.use_case.VassQuickUseCases
import com.vassteam2.vassquick.domain.util.SecureStorage
import com.vassteam2.vassquick.domain.util.Storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import formatDateToShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import parseDateToCalendar
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val useCases: VassQuickUseCases,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _allChats = MutableStateFlow<List<Chat>>(emptyList())

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    private val _filteredChats = MutableStateFlow<List<Chat>>(emptyList())
    val filteredChats: StateFlow<List<Chat>> = _filteredChats.asStateFlow()


    init {
        loadChatsAndLastMessages()
    }

    fun loadChatsAndLastMessages() {
        viewModelScope.launch {

            async(Dispatchers.Main) {
                _state.update {
                    it.copy(
                        isRefreshing = true
                    )
                }
            }
            val token = UserSingleton.token
            val response = withContext(Dispatchers.IO) {
                useCases.chatUseCase(token = token)
            }
            when (response) {
                is ApiResponse.Success -> {
                    val chatsWithLastMessageAndDate = response.data
                        .mapNotNull { chat ->
                            val lastMessageResult = useCases.getLastMessageUseCase(token, chat.id)
                            lastMessageResult?.let {
                                val messageCalendar = parseDateToCalendar(it.date)
                                chat.copy(
                                    lastMessage = it.message,
                                    lastMessageDate = formatDateToShow(it.date),
                                    lastMessageCalendar = messageCalendar
                                )
                            }
                        }.sortedWith(compareByDescending { it.lastMessageCalendar?.timeInMillis })

                    _filteredChats.update {
                        chatsWithLastMessageAndDate
                    }
                    _allChats.update {
                        chatsWithLastMessageAndDate
                    }
                    _state.update {
                        it.copy(
                            isRefreshing = false
                        )
                    }
                }

                is ApiResponse.Error -> {
                    _state.update {
                        it.copy(
                            isRefreshing = false
                        )
                    }
                }

            }
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {

            is ChatEvent.OnLongItemClick -> {
                _state.update {
                    it.copy(
                        showDeleteDialog = true,
                        selectedChatToDelete = event.chat
                    )
                }
            }

            is ChatEvent.ConfirmDeleteChat -> {
                deleteSelectedChat(chat = event.chat)
            }

            is ChatEvent.CancelDeleteChat -> {
                _state.update {
                    it.copy(
                        showDeleteDialog = false,
                        selectedChatToDelete = null
                    )
                }
            }

            is ChatEvent.ShowLogoutConfirmation -> {
                _state.update {
                    it.copy(
                        showLogoutDialog = true,
                        showDeleteDialog = false
                    )
                }
            }

            is ChatEvent.ClearSearchAndReloadChats -> {
                clearSearchAndReloadChats()
            }

            is ChatEvent.SetSearchQuery -> {
                _searchQuery.update {
                    event.query
                }
                updateFilteredChats()
            }

            is ChatEvent.ConfirmLogout -> {
                logout()
                _state.update {
                    it.copy(
                        showLogoutDialog = false
                    )
                }
            }

            is ChatEvent.DismissLogoutConfirmation -> {
                _state.update {
                    it.copy(
                        showLogoutDialog = false
                    )
                }
            }

            is ChatEvent.UpdateChatsOnlineStatus -> {
                updateChatsOnlineStatus()
            }

            is ChatEvent.ResetLoading -> {
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }

            is ChatEvent.Refresh -> {
                loadChatsAndLastMessages()
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            val token = UserSingleton.token
            val response = withContext(Dispatchers.IO) {
                useCases.logoutUseCase(authtoken = token)
            }
            when (response) {
                is ApiResponse.Success -> {
                    UserSingleton.clear()
                    SecureStorage.delete(context = context, key = TOKEN_KEY)
                    Storage.delete(context = context, key = HAVE_BIOMETRIC)
                    _state.update {
                        it.copy(
                            navigateToLogin = true
                        )
                    }
                }

                is ApiResponse.Error -> {
                }
            }
        }
    }

    private fun deleteSelectedChat(chat: Chat) {
        viewModelScope.launch {
            val token = UserSingleton.token
            val response = withContext(Dispatchers.IO) {
                useCases.deleteChatUseCase(id = chat.id, token = token)
            }
            when (response) {
                is ApiResponse.Success -> {
                    _state.update {
                        it.copy(
                            selectedChatToDelete = null
                        )
                    }
                    loadChatsAndLastMessages()
                }

                is ApiResponse.Error -> {
                }
            }
        }
    }


    private fun updateChatsOnlineStatus() {
        viewModelScope.launch {
            val token = UserSingleton.token
            val response = withContext(Dispatchers.IO) {
                useCases.chatUseCase(token = token)
            }
            when (response) {
                is ApiResponse.Success -> {
                    val updatedChats = response.data.map { chat ->
                        chat.copy(
                            targetonline = chat.targetonline == true && chat.sourceonline == true
                        )
                    }
                    _allChats.update {
                        updatedChats
                    }
                }

                is ApiResponse.Error -> {

                }
            }
        }
    }

    private fun updateFilteredChats() {
        val query = _searchQuery.value
        _filteredChats.update {
            if (query.isEmpty()) {
                _allChats.value
            } else {
                _allChats.value.filter {
                    it.targetnick?.contains(query, ignoreCase = true) ?: false
                }
            }
        }
    }

    private fun filterChats(query: String) {
        val filteredChats = if (query.isEmpty()) {
            _allChats.value
        } else {
            _allChats.value.filter { chat ->
                chat.targetnick?.contains(query, ignoreCase = true) ?: false
            }
        }
        _state.update {
            it.copy(
                filteredChats = filteredChats
            )
        }
    }

    private fun clearSearchAndReloadChats() {
        _state.update {
            it.copy(
                searchQuery = ""
            )
        }
        filterChats(query = "")
    }
}

