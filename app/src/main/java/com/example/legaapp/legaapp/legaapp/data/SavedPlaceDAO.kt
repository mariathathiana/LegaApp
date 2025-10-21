package com.example.legaapp.legaapp.legaapp.data



import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.legaapp.data.SavedPlace
import com.example.legaapp.legaapp.legaapp.utils.DatabaseManager

class SavedPlaceDAO(val context: Context) {

    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    private fun close() {
        db.close()
    }

    private fun getContentValues(place: SavedPlace): ContentValues {
        val values = ContentValues()
        values.put(SavedPlace.COLUMN_NAME, place.name)
        values.put(SavedPlace.COLUMN_LAT, place.latitude)
        values.put(SavedPlace.COLUMN_LON, place.longitude)
        return values
    }

    private fun readFromCursor(cursor: Cursor): SavedPlace {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(SavedPlace.COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(SavedPlace.COLUMN_NAME))
        val lat = cursor.getDouble(cursor.getColumnIndexOrThrow(SavedPlace.COLUMN_LAT))
        val lon = cursor.getDouble(cursor.getColumnIndexOrThrow(SavedPlace.COLUMN_LON))
        return SavedPlace(id, name, lat, lon)
    }

    fun insert(place: SavedPlace) {
        val values = getContentValues(place)
        try {
            open()
            db.insert(SavedPlace.TABLE_NAME, null, values)
        } finally {
            close()
        }
    }

    fun findAll(): List<SavedPlace> {
        val places = mutableListOf<SavedPlace>()
        try {
            open()
            val cursor = db.query(SavedPlace.TABLE_NAME, null, null, null, null, null, null)
            while (cursor.moveToNext()) {
                places.add(readFromCursor(cursor))
            }
        } finally {
            close()
        }
        return places
    }
}
