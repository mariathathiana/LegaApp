package com.example.legaapp.legaapp.legaapp.data



import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.legaapp.data.SavedPlace
import com.example.legaapp.legaapp.legaapp.activities.MapActivity
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
        values.put("name", place.name)
        values.put("latitude", place.latitude)
        values.put("longitude", place.longitude)
        return values
    }

    private fun readFromCursor(cursor: Cursor): SavedPlace {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        val lat = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"))
        val lon = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))
        return SavedPlace(id, name, lat, lon)
    }

    fun insert(place: SavedPlace) {
        try {
            open()
            val values = getContentValues(place)
            val result = db.insert("saved_places", null, values)
            if (result == -1L) {
                Log.e("SavedPlaceDAO", "Error insertando lugar: $place")
            } else {
                Log.d("SavedPlaceDAO", "Lugar insertado con id $result")
            }
        } catch (e: Exception) {
            Log.e("SavedPlaceDAO", "Error en insert", e)
        } finally {
            close()
        }
    }

    fun findAll(): List<SavedPlace> {
        val places = mutableListOf<SavedPlace>()
        try {
            open()
            val cursor = db.query("saved_places", null, null, null, null, null, null)
            while (cursor.moveToNext()) {
                places.add(readFromCursor(cursor))
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("SavedPlaceDAO", "Error en findAll", e)
        } finally {
            close()
        }
        Log.d("SavedPlaceDAO", "findAll retornó ${places.size} lugares")
        return places
    }

}





