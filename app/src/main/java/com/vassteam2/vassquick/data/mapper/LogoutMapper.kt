package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.response.LogoutResponse
import com.vassteam2.vassquick.domain.model.Logout

fun LogoutResponse.toLogout() : Logout{
    return Logout(
        message = this.message
    )
}