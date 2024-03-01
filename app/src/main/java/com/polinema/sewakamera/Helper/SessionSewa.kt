package com.polinema.sewakamera.Helper

import android.content.Context
import android.content.SharedPreferences

class SessionSewa(var context: Context?) {

    // Shared pref mode
    val PRIVATE_MODE = 0

    // Sharedpref file name
    private val PREF_NAME = "SharedPreferences"

    private val IS_LOGIN = "is_login"

    var pref: SharedPreferences? = context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor: SharedPreferences.Editor? = pref?.edit()

    fun setLoggin(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun setUsername(username: String?) {
        editor?.putString("username", username)
        editor?.commit()
    }

    fun setIdUser(idUser: String?) {
        editor?.putString("user_id", idUser)
        editor?.commit()
    }

    fun isLogin(): Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun getUsername(): String? {
        return pref?.getString("username", null)
    }

    fun getIdUser(): String? {
        return pref?.getString("user_id", null)
    }

    fun removeData() {
        editor?.clear()
        editor?.commit()
    }
}