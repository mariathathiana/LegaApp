package com.example.legaapp.legaapp.legaapp.utils

import com.example.legaapp.legaapp.legaapp.data.SavedPlaceDAO

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.legaapp.data.SavedPlace

class DatabaseManager(context: Context) :
    SQLiteOpenHelper(context, "places.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // habilitar soporte de foreign keys, si vas a usar relaciones entre tablas
        db.execSQL("PRAGMA foreign_keys = ON;")
        // crear tabla de lugares
        db.execSQL(SavedPlace.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // aquí manejas los cambios de esquema entre versiones
        db.execSQL(SavedPlace.SQL_DROP_TABLE)
        onCreate(db)
    }

    // opcional si quieres un método para borrar todas las tablas
    fun dropAll(db: SQLiteDatabase) {
        db.execSQL(SavedPlace.SQL_DROP_TABLE)
    }
}
