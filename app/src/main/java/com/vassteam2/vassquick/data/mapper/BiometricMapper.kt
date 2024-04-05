package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.response.BiometricResponse
import com.vassteam2.vassquick.domain.model.Login

fun BiometricResponse.toLogin(): Login = Login(
    token = token,
    user = userResponse.toUser())
