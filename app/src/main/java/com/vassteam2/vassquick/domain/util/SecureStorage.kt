package com.vassteam2.vassquick.domain.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.vassteam2.vassquick.domain.constants.IV
import com.vassteam2.vassquick.domain.constants.TOKEN_KEY
import javax.crypto.SecretKey

object SecureStorage {
    private fun createEncryptedSharedPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            "encrypted-store",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun save(context: Context, key: String, value: String) {
        val prefs = createEncryptedSharedPreferences(context)
        with(prefs.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun get(context: Context, key: String): String? {
        val prefs = createEncryptedSharedPreferences(context)
        return prefs.getString(key, null)
    }

    fun getIv(context: Context): ByteArray? {
        val prefs = createEncryptedSharedPreferences(context)
        val ivBase64 = prefs.getString(IV, null)
        return ivBase64?.let { Base64.decode(it, Base64.DEFAULT) }
    }

    fun contain(context: Context, key: String): Boolean {
        return try {
            val prefs = createEncryptedSharedPreferences(context)
            prefs.contains(key)
        } catch (e: Exception) {
            false
        }
    }

    fun delete(context: Context, key: String) {
        if (contain(context = context, key = key)) {
            val prefs = createEncryptedSharedPreferences(context)
            with(prefs.edit()) {
                remove(key)
                commit()
            }
        }
    }
}