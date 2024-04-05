package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.response.UpdateProfileResponse
import com.vassteam2.vassquick.domain.model.UpdateProfile

fun UpdateProfileResponse.toUpdateProfile(): UpdateProfile {
    val isSuccess = this.success.toBooleanStrictOrNull() ?: false
    return UpdateProfile(isSuccess = isSuccess)
}