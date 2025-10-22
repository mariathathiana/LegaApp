package com.example.legaapp.legaapp.legaapp.utils

import com.example.legaapp.legaapp.legaapp.data.SavedPlaceDAO

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.legaapp.data.SavedPlace

class DatabaseManager(context: Context) :
    SQLiteOpenHelper(context, "places.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("DatabaseManager", "onCreate - Creando tabla saved_places")
        db.execSQL("PRAGMA foreign_keys = ON;")
        val createTable = """
        CREATE TABLE saved_places (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            latitude REAL NOT NULL,
            longitude REAL NOT NULL
        )
    """.trimIndent()
        db.execSQL(createTable)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // aquí manejas los cambios de esquema entre versiones
        Log.d("DatabaseManager", "onUpgrade - Actualizando DB de versión $oldVersion a $newVersion")
        db.execSQL("DROP TABLE IF EXISTS saved_places")
        onCreate(db)
    }

    // opcional si quieres un método para borrar todas las tablas
    fun dropAll(db: SQLiteDatabase) {
        Log.d("DatabaseManager", "dropAll - Eliminando tablas")
        db.execSQL(SavedPlace.SQL_DROP_TABLE)
    }
}
