package com.vassteam2.vassquick.domain.util

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Base64
import com.vassteam2.vassquick.domain.constants.IV
import com.vassteam2.vassquick.domain.constants.TOKEN_KEY
import com.vassteam2.vassquick.domain.model.Response
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

private const val KEY_SIZE = 256
private const val ANDROID_KEYSTORE = "AndroidKeyStore"
private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

private fun getCipher(): Cipher {
    val transformation = (ENCRYPTION_ALGORITHM + "/"
            + ENCRYPTION_BLOCK_MODE + "/"
            + ENCRYPTION_PADDING)
    return Cipher.getInstance(transformation)
}

private fun getOrCreateSecretKey(keyName: String): SecretKey {
    val timeout = 30
    val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
    keyStore.load(null)
    keyStore.getKey(keyName, null)?.let { return it as SecretKey }

    val keyGenParamsBuilder = KeyGenParameterSpec.Builder(
        keyName,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(ENCRYPTION_BLOCK_MODE)
        .setEncryptionPaddings(ENCRYPTION_PADDING)
        .setUserAuthenticationRequired(true)
        .setKeySize(KEY_SIZE)
        .setInvalidatedByBiometricEnrollment(true)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        keyGenParamsBuilder.setUserAuthenticationParameters(
            timeout,
            KeyProperties.AUTH_BIOMETRIC_STRONG
        )
    } else {
        keyGenParamsBuilder.setUserAuthenticationValidityDurationSeconds(timeout)
    }

    val keyGenParams = keyGenParamsBuilder.build()

    val keyGenerator = KeyGenerator.getInstance(
        ENCRYPTION_ALGORITHM,
        ANDROID_KEYSTORE
    )
    keyGenerator.init(keyGenParams)
    return keyGenerator.generateKey()
}


private fun getInitializedCipherForEncryption(keyName: String, context: Context): Response<Cipher> {
    try {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName = keyName)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return Response.Success(data = cipher)
    } catch (e: KeyPermanentlyInvalidatedException) {
        deleteSecretKey(keyName = keyName)
        SecureStorage.delete(context = context, key = IV)
        SecureStorage.delete(context = context, key = TOKEN_KEY)
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName = keyName)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return Response.Success(data = cipher)
    } catch (e: Exception) {
        return Response.Error(exception = e)
    }
}

private fun getInitializedCipherForDecryption(
    keyName: String,
    initializationVector: ByteArray,
    context: Context
): Response<Cipher> {
    return try {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keyName = keyName)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(initializationVector))
        Response.Success(data = cipher)
    } catch (e: KeyPermanentlyInvalidatedException) {
        deleteSecretKey(keyName = keyName)
        SecureStorage.delete(context = context, key = IV)
        SecureStorage.delete(context = context, key = TOKEN_KEY)
        Response.Error(
            exception = e
        )
    } catch (e: Exception) {
        Response.Error(exception = e)
    }
}

private fun deleteSecretKey(keyName: String) {
    try {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        if (keyStore.containsAlias(keyName)) {
            keyStore.deleteEntry(keyName)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun encryptResponse(key: String, value: String, context: Context): Boolean {
    return when (val cipher =
        getInitializedCipherForEncryption(keyName = key, context = context)) {
        is Response.Success -> {
            encrypt(cipher = cipher.data, token = value, key = key, context = context)
            true
        }

        is Response.Error -> {
            false
        }
    }
}

fun decryptResponse(key: String, context: Context): String {
    val cipherResponse = SecureStorage.getIv(context = context)?.let {
        getInitializedCipherForDecryption(
            keyName = TOKEN_KEY, initializationVector = it, context = context
        )
    }
    return when (cipherResponse) {
        is Response.Success -> {
            decrypt(cipher = cipherResponse.data, key = key, context = context)
        }

        is Response.Error -> {
            ""
        }

        else -> {
            ""
        }
    }
}

private fun encrypt(
    cipher: Cipher,
    token: String,
    key: String,
    context: Context
) {
    val tokenBytesEncrypted = cipher.doFinal(token.toByteArray())
    val tokenEncrypted = Base64.encodeToString(tokenBytesEncrypted, Base64.DEFAULT)
    if (SecureStorage.contain(context, key)) {
        SecureStorage.delete(context, key)
    }
    val ivBase64 = Base64.encodeToString(cipher.iv, Base64.DEFAULT)
    if (SecureStorage.contain(context, IV)) {
        SecureStorage.delete(context, IV)
        SecureStorage.save(context, IV, ivBase64)
    } else {
        SecureStorage.save(context, IV, ivBase64)
    }

    SecureStorage.save(context, key, tokenEncrypted)
}

private fun decrypt(cipher: Cipher, key: String, context: Context): String {
    val tokenEncrypted = SecureStorage.get(context, key)
    val tokenEncryptedBytes = Base64.decode(tokenEncrypted, Base64.DEFAULT)
    val tokenDecrypted = cipher.doFinal(tokenEncryptedBytes)
    return String(tokenDecrypted)
}
