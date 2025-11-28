package com.camo.kripto.utils

import android.content.Context
import androidx.biometric.BiometricManager as AndroidBiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Biometric Authentication Manager
 * Handles fingerprint and face unlock authentication
 */
class BiometricAuthManager(private val activity: FragmentActivity) {

    private val biometricManager = AndroidBiometricManager.from(activity)

    fun isBiometricAvailable(): Boolean {
        return when (biometricManager.canAuthenticate(AndroidBiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            AndroidBiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    fun authenticate(
        title: String = "Biometric Authentication",
        subtitle: String = "Verify your identity",
        negativeButtonText: String = "Cancel",
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(activity, executor,
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
                    onFailed()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeButtonText)
            .setConfirmationRequired(false)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    companion object {
        private const val PREF_BIOMETRIC_ENABLED = "pref_biometric_enabled"

        fun isBiometricEnabled(context: Context): Boolean {
            return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getBoolean(PREF_BIOMETRIC_ENABLED, false)
        }

        fun setBiometricEnabled(context: Context, enabled: Boolean) {
            context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .edit()
                .putBoolean(PREF_BIOMETRIC_ENABLED, enabled)
                .apply()
        }
    }
}
