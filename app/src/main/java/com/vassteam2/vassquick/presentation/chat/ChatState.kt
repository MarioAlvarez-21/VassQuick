package com.vassteam2.vassquick.presentation.chat

import com.vassteam2.vassquick.domain.model.Chat

data class ChatState (
    val chatList: List<Chat> = emptyList(),
    var showDeleteConfirmationDialog: Boolean = false,
    var showLogoutConfirmation : Boolean = false,
    val showDialogEditProfile: Boolean = false,
    var searchQuery: String = "",
    val showLogoutDialog: Boolean = false,
    val isUserAuthenticated: Boolean = false,
    val showDeleteDialog : Boolean = false,
    var selectedChatToDelete: Chat? = null,
    val onlineStatuses: Map<String, Boolean> = emptyMap(),
    var navigateToLogin: Boolean = false,
    var filteredChats: List<Chat> = emptyList(),
    var isLoading: Boolean = false,
    var isRefreshing: Boolean = false,
)