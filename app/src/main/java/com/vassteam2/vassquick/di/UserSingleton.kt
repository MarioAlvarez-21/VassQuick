package com.vassteam2.vassquick.di

import com.vassteam2.vassquick.domain.model.User

object UserSingleton {
    lateinit var token: String
    lateinit var user: User

    fun clear() {
        token = ""
        user = User("","","","","",false,"",
            "", "","", "")
    }
}