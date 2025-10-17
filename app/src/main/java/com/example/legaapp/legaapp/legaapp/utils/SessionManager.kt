package com.example.legaapp.legaapp.legaapp.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager (context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("lega_session", Context.MODE_PRIVATE)


    fun setFavorite(legaId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("FAVORITE_LEGA_ID", legaId)
        editor.apply()
    }

    fun getFavorite(): String {
        return sharedPreferences.getString("FAVORITE_LEGA_ID", "")!!
    }

    fun isFavorite(legaId: String?): Boolean {
        return legaId == getFavorite()
    }

}