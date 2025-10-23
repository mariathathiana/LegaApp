package com.example.legaapp.legaapp.legaapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.legaapp.legaapp.legaapp.R
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        return true
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
