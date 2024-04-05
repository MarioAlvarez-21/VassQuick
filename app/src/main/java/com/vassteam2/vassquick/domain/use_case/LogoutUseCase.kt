package com.vassteam2.vassquick.domain.use_case

import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.Logout
import com.vassteam2.vassquick.domain.repository.VassQuickRepository

class LogoutUseCase(val repository: VassQuickRepository) {
    suspend operator fun invoke(authtoken : String): ApiResponse<Logout> {
        return repository.logout(token = authtoken)
    }
}