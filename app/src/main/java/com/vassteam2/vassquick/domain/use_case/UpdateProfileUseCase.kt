package com.vassteam2.vassquick.domain.use_case

import com.vassteam2.vassquick.data.request.UpdateProfileRequest
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.model.UpdateProfile
import com.vassteam2.vassquick.domain.repository.VassQuickRepository

class UpdateProfileUseCase(val repository: VassQuickRepository) {
    suspend operator fun invoke(token: String, updateProfileRequest: UpdateProfileRequest): ApiResponse<UpdateProfile>{
        return repository.updateProfile(token = token, updateProfileRequest = updateProfileRequest)
    }

}