package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.response.UserResponse
import com.vassteam2.vassquick.domain.model.User

fun UserResponse.toUser(): User {
    return User(
        id = this.id,
        nick = this.nick,
        avatar = this.avatar,
        online = this.online,
        token = this.token,
        login = this.login,
        password = this.password,
        created = this.created,
        platform = this.platform,
        updated = this.updated,
        uuid = this.uuid
    )
}