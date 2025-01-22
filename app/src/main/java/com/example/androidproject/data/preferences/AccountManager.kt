package com.example.androidproject.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.androidproject.model.LoginResponse
import com.google.gson.Gson

object AccountManager {
    private const val PREF_NAME = "account_data"
    private const val ACCOUNT_KEY = "account"
    private lateinit var preferences: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveAccount(account: LoginResponse) {
        val accountJson = gson.toJson(account)
        Log.d("AccountManager", "Saving account: $accountJson")
        preferences.edit().putString(ACCOUNT_KEY, accountJson).apply()
    }

    fun getAccount(): LoginResponse? {
        val accountJson = preferences.getString(ACCOUNT_KEY, null)
        return if (!accountJson.isNullOrEmpty()) {
            Log.d("AccountManager", "Retrieved account: $accountJson")
            gson.fromJson(accountJson, LoginResponse::class.java)
        } else {
            null
        }
    }

    fun clearAccountData() {
        preferences.edit().remove(ACCOUNT_KEY).apply()
    }
}