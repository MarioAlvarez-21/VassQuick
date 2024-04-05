package com.vassteam2.vassquick.domain.util

import android.app.Activity
import android.content.Context
import android.util.Base64
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.biometric.BiometricManager
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.domain.constants.IV
import java.util.concurrent.Executor
import javax.crypto.Cipher

fun showBiometricPrompt(
    context: Context,
    executor: Executor,
    onSuccess: () -> Unit,
    onError: (String) -> Unit,
) {
    val activity = (context as? Activity)
    val biometricPrompt = BiometricPrompt(
        activity as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errString.toString())
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onError(context.getString(R.string.authentication_failed))
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(context.getString(R.string.biometric_authentication))
        .setSubtitle(context.getString(R.string.log_in_using_your_fingerprint_or_facial_recognition))
        .setNegativeButtonText(context.getString(R.string.cancel))
        .build()

    biometricPrompt.authenticate(promptInfo)
}

fun hasBiometricCapability(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    when (biometricManager.canAuthenticate()) {
        BiometricManager.BIOMETRIC_SUCCESS ->
            return true

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
            return false
    }
    return false
}

