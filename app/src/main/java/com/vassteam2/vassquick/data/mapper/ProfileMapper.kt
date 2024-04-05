package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.response.ProfileResponse
import com.vassteam2.vassquick.domain.model.User

fun ProfileResponse.toUser(): User {
    return User(
        id = this.id,
        login = this.login,
        password = this.password,
        nick = this.nick,
        avatar = this.avatar,
        platform = this.platform,
        uuid = this.uuid,
        token = this.token,
        online = this.online,
        created = this.created,
        updated = this.updated
    )
}