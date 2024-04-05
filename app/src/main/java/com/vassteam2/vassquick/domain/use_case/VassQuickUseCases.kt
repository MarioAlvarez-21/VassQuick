package com.vassteam2.vassquick.domain.use_case

import GetLastMessageUseCase

data class VassQuickUseCases(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val deleteChatUseCase: DeleteChatUseCase,
    val chatUseCase: GetChatsUseCase,
    val usersUseCase: UsersUseCase,
    val createdChatUseCase: CreatedChatUsecase,
    val getProfileUseCase: GetProfileUseCase,
    val updateProfileUseCase: UpdateProfileUseCase,
    val getMessagesUseCase: GetMessagesUseCase,
    val getChatByIdUseCase: GetChatByIdUseCase,
    val getUserByIdUseCase: GetUserByIdUseCase,
    val postNewMessageUseCase: PostNewMessageUseCase,
    val biometricUseCase: BiometricUseCase,
    val logoutUseCase: LogoutUseCase,
    val getLastMessageUseCase: GetLastMessageUseCase
)
