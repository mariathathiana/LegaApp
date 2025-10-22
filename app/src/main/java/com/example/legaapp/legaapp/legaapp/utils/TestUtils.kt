package com.example.legaapp.legaapp.legaapp.utils

import android.content.Context
import android.util.Log
import com.example.legaapp.data.SavedPlace
import com.example.legaapp.legaapp.legaapp.data.SavedPlaceDAO

class TestUtils{


    private fun testSavedPlaceDAO() {
        val dao = SavedPlaceDAO(this)

        val placeToInsert = SavedPlace(
            id = 0,
            name = "Lugar de prueba",
            latitude = -34.6037,
            longitude = -58.3816
        )

        dao.insert(placeToInsert)
        Log.d("TestDAO", "Lugar insertado: $placeToInsert")

        val savedPlaces = dao.findAll()
        Log.d("TestDAO", "Lugares guardados (${savedPlaces.size}):")
        savedPlaces.forEach {
            Log.d("TestDAO", " - ${it.name} (${it.latitude}, ${it.longitude})")
        }
    }


}