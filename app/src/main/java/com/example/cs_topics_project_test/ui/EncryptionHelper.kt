package com.example.cs_topics_project_test.ui;

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionHelper {
    private const val secretKey = "12345678901234567890123456789012" // 32 characters (256 bits)
    private const val initVector = "abcdefghijklmnop" // 16 characters (128 bits)

    private fun getSecretKeySpec(): SecretKeySpec {
        return SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
    }

    fun encrypt(value: String): String {
        val iv = IvParameterSpec(initVector.toByteArray(Charsets.UTF_8))
        val skeySpec = getSecretKeySpec()

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)

        val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    fun decrypt(encrypted: String): String {
        val iv = IvParameterSpec(initVector.toByteArray(Charsets.UTF_8))
        val skeySpec = getSecretKeySpec()

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)

        val original = cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP))
        return String(original, Charsets.UTF_8)
    }
}


