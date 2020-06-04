package com.adrianmulyawan.bwamov.utils

import android.content.Context
import android.content.SharedPreferences

class Preferences (val context: Context) {
    companion object {
        const val MEETING_PREF = "USER_PREF"
    }

    val sharedPref = context.getSharedPreferences(MEETING_PREF, 0)
    // val sharedPref merupakan sebuah key

    // mengeset nilai berdasarkan key
    fun setValue(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    // mengambil data berdasarkan key
    fun getValue(key: String) : String? {
        return sharedPref.getString(key, null)
    }
}