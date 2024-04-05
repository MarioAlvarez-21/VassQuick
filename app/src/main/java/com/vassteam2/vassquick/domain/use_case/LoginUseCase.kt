package com.vassteam2.vassquick.domain.use_case

import com.vassteam2.vassquick.data.request.LoginRequest
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.Login
import com.vassteam2.vassquick.domain.repository.VassQuickRepository

class LoginUseCase(val repository: VassQuickRepository) {

    suspend operator fun invoke(login: String, password: String): ApiResponse<Login> {
        return repository.login(loginRequest = LoginRequest(login = login, password = password))
    }
}