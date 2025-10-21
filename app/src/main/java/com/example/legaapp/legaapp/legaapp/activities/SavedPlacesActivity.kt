package com.example.legaapp.legaapp.legaapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.legaapp.data.SavedPlace
import com.example.legaapp.legaapp.legaapp.adapters.SavedPlacesAdapter
import com.example.legaapp.legaapp.legaapp.data.SavedPlaceDAO
import com.example.legaapp.legaapp.legaapp.databinding.ActivitySavedPlacesBinding

class SavedPlacesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedPlacesBinding
    private lateinit var adapter: SavedPlacesAdapter
    private lateinit var dao: SavedPlaceDAO
    private var places: List<MapActivity.SavedPlace> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lugares guardados"

        dao = SavedPlaceDAO(this)
        setupRecyclerView()
        loadPlaces()
    }

    private fun setupRecyclerView() {
        binding.rvSavedPlaces.layoutManager = LinearLayoutManager(this)
        adapter = SavedPlacesAdapter(places) { place ->
            onPlaceClicked(place)
        }
        binding.rvSavedPlaces.adapter = adapter
    }

    private fun loadPlaces() {
        Thread {
            val mapPlaces = dao.findAll() // Suponiendo que findAll() devuelve List<MapActivity.SavedPlace>
            val savedPlaces = mapPlaces.map { mapPlace ->
                MapActivity.SavedPlace(mapPlace.name, mapPlace.latitude, mapPlace.longitude)
            }
            runOnUiThread {
                adapter.updatePlaces(savedPlaces)
            }
        }.start()
    }


    private fun onPlaceClicked(place: SavedPlace) {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("LATITUDE", place.latitude)
        intent.putExtra("LONGITUDE", place.longitude)
        intent.putExtra("TITLE", place.name)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
