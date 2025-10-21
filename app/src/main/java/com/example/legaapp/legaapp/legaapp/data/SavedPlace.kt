package com.example.legaapp.data

data class SavedPlace(
    val id: Int,
    var name: String,
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        const val TABLE_NAME = "SavedPlace"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_LAT = "latitude"
        const val COLUMN_LON = "longitude"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME TEXT," +
                    "$COLUMN_LAT REAL," +
                    "$COLUMN_LON REAL)"

        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
