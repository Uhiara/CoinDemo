package com.example.coindemo.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val context: Context
) {
    private val preferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    suspend fun isOnboardingComplete(): Boolean {
        return withContext(Dispatchers.IO) {
            preferences.getBoolean("onboarding_complete", false)
        }
    }

    suspend fun setOnboardingComplete(complete: Boolean) {
        withContext(Dispatchers.IO) {
            preferences.edit().putBoolean("onboarding_complete", complete).apply()
        }
    }
}