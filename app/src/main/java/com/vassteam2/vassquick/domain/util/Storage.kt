package com.vassteam2.vassquick.domain.util

import android.content.Context
import android.content.SharedPreferences

object Storage {

    private const val PREFERENCES_NAME = "unencrypted-store"

    private fun createUnencryptedSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun save(context: Context, key: String, value: String) {
        val prefs = createUnencryptedSharedPreferences(context)
        with(prefs.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun get(context: Context, key: String): String? {
        val prefs = createUnencryptedSharedPreferences(context)
        return prefs.getString(key, null)
    }

    fun contain(context: Context, key: String): Boolean {
        val prefs = createUnencryptedSharedPreferences(context)
        return prefs.contains(key)
    }

    fun delete(context: Context, key: String) {
        if (contain(context = context, key = key)){
            val prefs = createUnencryptedSharedPreferences(context)
            with(prefs.edit()) {
                remove(key)
                commit()
            }
        }
    }
}