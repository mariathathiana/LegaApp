package com.example.legaapp.legaapp.legaapp.data



import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.legaapp.legaapp.legaapp.utils.DatabaseManager




class SavedPlaceDAO(val context: Context) {
    private lateinit var db: SQLiteDatabase

    companion object {
        const val TABLE_NAME = "saved_places"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
    }


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

    fun delete(id: Int) {
        try {
            open()
            val deletedRows = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
            Log.i("SavedPlaceDAO", "$deletedRows rows deleted in table $TABLE_NAME")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun update(savedplace: SavedPlace) {
        // Create a new map of values, where column names are the keys
        val values = getContentValues(savedplace)

        try {
            open()

            // Insert the new row, returning the primary key value of the new row
            val updatedRows = db.update(SavedPlace.TABLE_NAME, values, "${SavedPlace.COLUMN_ID} = ${savedplace.id}", null)
            Log.i("DATABASE", "$updatedRows rows updated in table ${SavedPlace.TABLE_NAME}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

}





