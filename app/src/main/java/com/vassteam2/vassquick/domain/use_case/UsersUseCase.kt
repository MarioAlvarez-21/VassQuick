package com.vassteam2.vassquick.domain.use_case

import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.User
import com.vassteam2.vassquick.domain.repository.VassQuickRepository

class UsersUseCase(val repository: VassQuickRepository) {
    suspend operator fun invoke(token: String): ApiResponse<List<User>> {
        return repository.getUsers(token = token)
    }
}