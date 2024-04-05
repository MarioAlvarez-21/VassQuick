package com.vassteam2.vassquick.domain.repository

import androidx.paging.PagingData
import com.vassteam2.vassquick.data.request.ChatRequest
import com.vassteam2.vassquick.data.request.LoginRequest
import com.vassteam2.vassquick.data.request.RegisterRequest
import com.vassteam2.vassquick.data.request.UpdateProfileRequest
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.Chat
import com.vassteam2.vassquick.domain.model.CreatedChat
import com.vassteam2.vassquick.domain.model.DeleteChat
import com.vassteam2.vassquick.domain.model.Login
import com.vassteam2.vassquick.domain.model.Logout
import com.vassteam2.vassquick.domain.model.Message
import com.vassteam2.vassquick.domain.model.NewMessage
import com.vassteam2.vassquick.domain.model.Register
import com.vassteam2.vassquick.domain.model.UpdateProfile
import com.vassteam2.vassquick.domain.model.User
import kotlinx.coroutines.flow.Flow


interface VassQuickRepository {
    suspend fun login(loginRequest: LoginRequest): ApiResponse<Login>
    suspend fun register(registerRequest: RegisterRequest): ApiResponse<Register>
    suspend fun getUsers(token: String): ApiResponse<List<User>>
    suspend fun newChat(chatRequest: ChatRequest, token: String): ApiResponse<CreatedChat>
    suspend fun getChats(token: String): ApiResponse<List<Chat>>
    suspend fun deleteChat(id: String, token: String): ApiResponse<DeleteChat>
    suspend fun getProfile(token: String): ApiResponse<User>
    fun getMessageStream(idChat: String, pageSize: Int, prefetchDistance: Int, token: String): Flow<PagingData<Message>>
    suspend fun getChatById(token: String, idChat: String): ApiResponse<Chat>
    suspend fun getUserById(token: String, idUser: String): ApiResponse<User>
    suspend fun postNewMessage(token: String, newMessage: NewMessage): ApiResponse<Boolean>
    suspend fun updateProfile (token: String, updateProfileRequest: UpdateProfileRequest): ApiResponse<UpdateProfile>
    suspend fun biometricLogin(token: String): ApiResponse<Login>
    suspend fun logout(token: String): ApiResponse<Logout>
    suspend fun getLastMessage(token: String, chatId: String): ApiResponse<Message>


}