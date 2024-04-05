package com.vassteam2.vassquick.di

import GetLastMessageUseCase
import com.vassteam2.vassquick.data.VassQuickService
import com.vassteam2.vassquick.data.repository_impl.VassQuickRepositoryImpl
import com.vassteam2.vassquick.domain.repository.VassQuickRepository
import com.vassteam2.vassquick.domain.use_case.BiometricUseCase
import com.vassteam2.vassquick.domain.use_case.CreatedChatUsecase
import com.vassteam2.vassquick.domain.use_case.DeleteChatUseCase
import com.vassteam2.vassquick.domain.use_case.GetChatByIdUseCase
import com.vassteam2.vassquick.domain.use_case.GetChatsUseCase
import com.vassteam2.vassquick.domain.use_case.GetMessagesUseCase
import com.vassteam2.vassquick.domain.use_case.GetProfileUseCase
import com.vassteam2.vassquick.domain.use_case.GetUserByIdUseCase
import com.vassteam2.vassquick.domain.use_case.LoginUseCase
import com.vassteam2.vassquick.domain.use_case.LogoutUseCase
import com.vassteam2.vassquick.domain.use_case.PostNewMessageUseCase
import com.vassteam2.vassquick.domain.use_case.RegisterUseCase
import com.vassteam2.vassquick.domain.use_case.UpdateProfileUseCase
import com.vassteam2.vassquick.domain.use_case.UsersUseCase
import com.vassteam2.vassquick.domain.use_case.VassQuickUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://mock-movilidad.vass.es/chatvass/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideVassService(retrofit: Retrofit): VassQuickService =
        retrofit.create(VassQuickService::class.java)

    @Provides
    @Singleton
    fun provideVassRepository(service: VassQuickService): VassQuickRepository {
        return VassQuickRepositoryImpl(service = service)
    }

    @Provides
    @Singleton
    fun provideVassUseCases(repository: VassQuickRepository): VassQuickUseCases {
        return VassQuickUseCases(
            loginUseCase = LoginUseCase(repository = repository),
            registerUseCase = RegisterUseCase(repository = repository),
            chatUseCase = GetChatsUseCase(repository = repository),
            usersUseCase = UsersUseCase(repository = repository),
            deleteChatUseCase = DeleteChatUseCase(repository = repository),
            createdChatUseCase = CreatedChatUsecase(repository = repository),
            getProfileUseCase = GetProfileUseCase(repository = repository),
            updateProfileUseCase = UpdateProfileUseCase(repository = repository),
            getMessagesUseCase = GetMessagesUseCase(repository = repository),
            getChatByIdUseCase = GetChatByIdUseCase(repository = repository),
            getUserByIdUseCase = GetUserByIdUseCase(repository = repository),
            postNewMessageUseCase = PostNewMessageUseCase(repository = repository),
            biometricUseCase = BiometricUseCase(repository = repository),
            logoutUseCase = LogoutUseCase(repository = repository),
            getLastMessageUseCase = GetLastMessageUseCase(repository = repository)
        )
    }
}