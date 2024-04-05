package com.vassteam2.vassquick.data.repository_impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vassteam2.vassquick.data.VassQuickService
import com.vassteam2.vassquick.data.mapper.executeApiCall
import com.vassteam2.vassquick.data.mapper.executeApiCallAndFilterById
import com.vassteam2.vassquick.data.mapper.executeApiCallList
import com.vassteam2.vassquick.data.mapper.toChat
import com.vassteam2.vassquick.data.mapper.toCreatedChat
import com.vassteam2.vassquick.data.mapper.toDeleteChat
import com.vassteam2.vassquick.data.mapper.toLogin
import com.vassteam2.vassquick.data.mapper.toLogout
import com.vassteam2.vassquick.data.mapper.toMessage
import com.vassteam2.vassquick.data.mapper.toNewMessageRequest
import com.vassteam2.vassquick.data.mapper.toRegister
import com.vassteam2.vassquick.data.mapper.toUpdateProfile
import com.vassteam2.vassquick.data.mapper.toUser
import com.vassteam2.vassquick.data.paging.MessagePagingSource
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
import com.vassteam2.vassquick.domain.repository.VassQuickRepository
import kotlinx.coroutines.flow.Flow

class VassQuickRepositoryImpl(private val service: VassQuickService) : VassQuickRepository {
    override suspend fun login(loginRequest: LoginRequest): ApiResponse<Login> {
        return executeApiCall(
            call = { service.doLogin(loginRequest = loginRequest) },
            map = { it.toLogin() })
    }

    override suspend fun register(registerRequest: RegisterRequest): ApiResponse<Register> {
        return executeApiCall(
            call = { service.doRegister(registerRequest = registerRequest) },
            map = { it.toRegister() })
    }

    override suspend fun getChats(token: String): ApiResponse<List<Chat>> {
        return executeApiCallList(
            call = { service.getChats(authToken = token) },
            map = { it.toChat() }
        )
    }

    override suspend fun getUsers(token: String): ApiResponse<List<User>> {
        return executeApiCallList(
            call = { service.getUsers(authToken = token) },
            map = { it.toUser() }
        )
    }

    override fun getMessageStream(
        idChat: String,
        pageSize: Int,
        prefetchDistance: Int,
        token: String
    ): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(pageSize = 8, prefetchDistance = 6),
            pagingSourceFactory = {
                MessagePagingSource(idChat = idChat, vassQuickService = service, token = token)
            }
        ).flow
    }

    override suspend fun newChat(
        chatRequest: ChatRequest,
        token: String
    ): ApiResponse<CreatedChat> {
        return executeApiCall(
            call = { service.newChat(authToken = token, chatRequest = chatRequest) },
            map = { it.toCreatedChat() }
        )
    }

    override suspend fun deleteChat(id: String, token: String): ApiResponse<DeleteChat> {
        return executeApiCall(
            call = { service.deleteChat(id = id, authToken = token) },
            map = { it.toDeleteChat() }
        )
    }

    override suspend fun getProfile(token: String): ApiResponse<User> {
        return executeApiCall(
            call = { service.getProfile(authToken = token) },
            map = { it.toUser() }
        )
    }

    override suspend fun updateProfile(
        token: String,
        updateProfileRequest: UpdateProfileRequest
    ): ApiResponse<UpdateProfile> {
        return executeApiCall(
            call = {
                service.updateProfile(
                    authToken = token,
                    updateProfileRequest = updateProfileRequest
                )
            },
            map = { it.toUpdateProfile() }
        )
    }

    override suspend fun biometricLogin(token: String): ApiResponse<Login> {
        return executeApiCall(
            call = { service.loginBiometric(authToken = token) },
            map = { it.toLogin() }
        )
    }

    override suspend fun logout(token: String): ApiResponse<Logout> {
        return executeApiCall(
            call = {service.logout(authToken = token)},
            map = {it.toLogout()}
        )
    }

    override suspend fun getLastMessage(token: String, chatId: String): ApiResponse<Message> {
        return try {
            val response = service.getMessages(authToken = token, id = chatId, offset = 0, limit = 1)
            if (response.isSuccessful && response.body() != null) {
                val lastMessageResponse = response.body()!!.messageResponses?.lastOrNull()
                if (lastMessageResponse != null) {
                    ApiResponse.Success(lastMessageResponse.toMessage())
                } else {
                    ApiResponse.Error(Exception("No messages found"))
                }
            } else {
                ApiResponse.Error(Exception("API call failed"))
            }
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }


    override suspend fun getChatById(token: String, idChat: String): ApiResponse<Chat> {
        return executeApiCallAndFilterById(
            call = { service.getChats(authToken = token) },
            map = { it.toChat() },
            filter = { it.chat == idChat }
        )
    }

    override suspend fun getUserById(token: String, idUser: String): ApiResponse<User> {
        return executeApiCallAndFilterById(
            call = { service.getUsers(authToken = token) },
            map = { it.toUser() },
            filter = { it.id == idUser }
        )
    }

    override suspend fun postNewMessage(
        token: String,
        newMessage: NewMessage
    ): ApiResponse<Boolean> {
        return executeApiCall(
            call = {
                service.postNewMessage(
                    authToken = token,
                    newMessageRequest = newMessage.toNewMessageRequest()
                )
            },
            map = { it.success }
        )
    }
}
