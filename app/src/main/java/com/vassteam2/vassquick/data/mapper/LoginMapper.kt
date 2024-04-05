package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.response.LoginResponse
import com.vassteam2.vassquick.domain.model.Login
import com.vassteam2.vassquick.domain.model.User
import java.util.UUID

fun LoginResponse.toLogin(): Login {
    return Login(
        token = this.token,
        user = User(
            avatar = this.userResponse.avatar,
            id = this.userResponse.id,
            nick = this.userResponse.nick,
            online = this.userResponse.online,
        )
    )
}
