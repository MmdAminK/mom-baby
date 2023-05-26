package com.app.mombaby.utils.utilities

import android.content.SharedPreferences

object PreferencesUtils {

    fun SharedPreferences.isStringValueEmpty(key: String): Boolean {
        return this.getString(key, "") == ""
    }
}