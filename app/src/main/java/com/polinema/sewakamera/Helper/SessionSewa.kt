package com.polinema.sewakamera.Helper

import android.content.Context
import android.content.SharedPreferences

class SessionSewa(var context: Context?) {

    companion object {
        private const val PREF_NAME = "LoginSession"
        private const val IS_LOGIN = "IsLoggedIn"
        private const val KEY_USER_ID = "UserId"
        private const val KEY_USERNAME = "Username"
        private const val KEY_IMAGE = "Image"
        private const val LAST_ACTIVE_TIME = "LastActiveTime"
//        private const val SESSION_TIMEOUT = 30 * 60 * 1000
        private const val SESSION_TIMEOUT = 60 * 1000
    }

    private var pref: SharedPreferences = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = pref.edit()

    /**
     * Set login status.
     */
    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(IS_LOGIN, isLoggedIn)
        editor.commit()
    }

    /**
     * Get login status.
     */
    fun getLogin(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    /**
     * Set user ID.
     */
    fun setUserId(userId: String) {
        editor.putString(KEY_USER_ID, userId)
        editor.commit()
    }

    /**
     * Get user ID.
     */
    fun getUserId(): String? {
        return pref.getString(KEY_USER_ID, null)
    }

    /**
     * Set username.
     */
    fun setUsername(username: String) {
        editor.putString(KEY_USERNAME, username)
        editor.commit()
    }

    /**
     * Get username.
     */
    fun getUsername(): String? {
        return pref.getString(KEY_USERNAME, null)
    }


    fun setImage(image: String) {
        editor.putString(KEY_IMAGE, image)
        editor.commit()
    }

    /**
     * Get username.
     */
    fun getImage(): String? {
        return pref.getString(KEY_IMAGE, null)
    }

    fun updateLastActiveTime() {
        editor.putLong(LAST_ACTIVE_TIME, System.currentTimeMillis())
        editor.commit()
    }

    /**
     * Check if the session is still valid.
     */
    private fun checkSessionValidity() {
        val lastActive = pref.getLong(LAST_ACTIVE_TIME, 0)
        if (System.currentTimeMillis() > lastActive + SESSION_TIMEOUT) {
            logoutUser() // Reset the session if the timeout has expired
        }
    }

    /**
     * Clear session details.
     */
    fun logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear()
        editor.commit()
    }
}