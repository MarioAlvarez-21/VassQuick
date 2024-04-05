package com.vassteam2.vassquick.data

import com.vassteam2.vassquick.data.request.ChatRequest
import com.vassteam2.vassquick.data.request.LoginRequest
import com.vassteam2.vassquick.data.request.NewMessageRequest
import com.vassteam2.vassquick.data.request.RegisterRequest
import com.vassteam2.vassquick.data.request.UpdateProfileRequest
import com.vassteam2.vassquick.data.response.BiometricResponse
import com.vassteam2.vassquick.data.response.ChatResponse
import com.vassteam2.vassquick.data.response.CreatedChatResponse
import com.vassteam2.vassquick.data.response.DeleteChatResponse
import com.vassteam2.vassquick.data.response.LoginResponse
import com.vassteam2.vassquick.data.response.LogoutResponse
import com.vassteam2.vassquick.data.response.MessagesResponse
import com.vassteam2.vassquick.data.response.NewMessageResponse
import com.vassteam2.vassquick.data.response.ProfileResponse
import com.vassteam2.vassquick.data.response.RegisterResponse
import com.vassteam2.vassquick.data.response.UpdateProfileResponse
import com.vassteam2.vassquick.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface VassQuickService {
    @POST("api/users/login")
    suspend fun doLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/users/register")
    suspend fun doRegister(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("api/chats")
    suspend fun newChat(
        @Header("Authorization") authToken: String,
        @Body chatRequest: ChatRequest
    ): Response<CreatedChatResponse>

    @GET("api/chats/view")
    suspend fun getChats(@Header("Authorization") authToken: String): Response<List<ChatResponse>>

    @DELETE("api/chats/{id}")
    suspend fun deleteChat(
        @Path("id") id: String,
        @Header("Authorization") authToken: String
    ): Response<DeleteChatResponse>

    @GET("api/users/profile")
    suspend fun getProfile(@Header("Authorization") authToken: String): Response<ProfileResponse>

    @PUT("api/users/profile")
    suspend fun updateProfile(
        @Header("Authorization") authToken: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): Response<UpdateProfileResponse>

    @GET("api/messages/list/{id}")
    suspend fun getMessages(
        @Header("Authorization") authToken: String,
        @Path("id") id: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<MessagesResponse>

    @GET("api/users")
    suspend fun getUsers(@Header("Authorization") authToken: String): Response<List<UserResponse>>
    @POST("api/users/biometric")
    suspend fun loginBiometric(@Header("Authorization") authToken: String): Response<BiometricResponse>

    @POST("api/messages/new")
    suspend fun postNewMessage(
        @Header("Authorization") authToken: String,
        @Body newMessageRequest: NewMessageRequest
    ): Response<NewMessageResponse>

    @POST("api/users/logout")
    suspend fun logout(@Header("Authorization")authToken: String) : Response<LogoutResponse>
}