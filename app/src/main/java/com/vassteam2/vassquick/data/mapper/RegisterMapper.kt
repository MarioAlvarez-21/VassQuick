package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.response.RegisterResponse
import com.vassteam2.vassquick.domain.model.Register
import com.vassteam2.vassquick.domain.model.User

fun RegisterResponse.toRegister(): Register {
    return Register(
        success = this.success,
        user = User(
            id = this.userResponse.id,
            nick = this.userResponse.nick,
            avatar = this.userResponse.avatar,
            online = this.userResponse.online
        )
    )
}