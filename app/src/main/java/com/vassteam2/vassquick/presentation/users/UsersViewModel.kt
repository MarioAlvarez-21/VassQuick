package com.vassteam2.vassquick.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.di.UserSingleton
import com.vassteam2.vassquick.domain.use_case.VassQuickUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val useCases: VassQuickUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(UsersState())
    val state = _state.asStateFlow()

    init {
        loadUserList()
    }

    fun onEvent(usersEvent: UsersEvent) {
        when (usersEvent) {
            is UsersEvent.ClickUser -> {
                createdChat(id = usersEvent.user.id)
            }

            is UsersEvent.ResetLoading -> {
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun loadUserList() {
        val token = UserSingleton.token
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                useCases.usersUseCase(token)
            }
            when (response) {
                is ApiResponse.Success -> {
                    _state.update {
                        it.copy(
                            users = response.data
                        )
                    }
                }

                is ApiResponse.Error -> {
                }
            }
        }
    }

    private fun createdChat(id: String) {
        val token = UserSingleton.token
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                useCases.createdChatUseCase(
                    source = UserSingleton.user.id,
                    target = id,
                    token = token

                )
            }
            when (response) {
                is ApiResponse.Success -> {
                    _state.update {
                        it.copy(
                            idChat = response.data.chat.id,
                            isLoading = true
                        )
                    }
                }

                is ApiResponse.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}