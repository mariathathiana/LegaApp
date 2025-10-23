package com.example.legaapp.legaapp.legaapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.legaapp.legaapp.legaapp.adapters.SavedPlacesAdapter
import com.example.legaapp.legaapp.legaapp.data.SavedPlaceDAO
import com.example.legaapp.legaapp.legaapp.databinding.ActivitySavedPlacesBinding
import com.example.legaapp.legaapp.legaapp.data.SavedPlace

class SavedPlacesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedPlacesBinding
    private lateinit var adapter: SavedPlacesAdapter
    private lateinit var dao: SavedPlaceDAO
    private var places: List<SavedPlace> = listOf()


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
            val savedPlaces = dao.findAll()

            Log.d("SavedPlacesActivity", "Se encontraron ${savedPlaces.size} lugares guardados")

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
