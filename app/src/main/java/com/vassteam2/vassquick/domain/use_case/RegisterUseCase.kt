package com.vassteam2.vassquick.domain.use_case

import com.vassteam2.vassquick.data.request.LoginRequest
import com.vassteam2.vassquick.data.request.RegisterRequest
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.Login
import com.vassteam2.vassquick.domain.model.Register
import com.vassteam2.vassquick.domain.repository.VassQuickRepository

class RegisterUseCase(val repository: VassQuickRepository) {
    suspend operator fun invoke(login: String, password: String, nick: String): ApiResponse<Register> {
        return repository.register(registerRequest = RegisterRequest(login = login, password = password, nick = nick))
    }
}