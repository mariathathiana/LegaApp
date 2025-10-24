package com.example.legaapp.legaapp.legaapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
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
        adapter = SavedPlacesAdapter(
            places,
            onItemClick = { place -> onPlaceClicked(place) },
            onDeleteClick = { place -> showDeleteConfirmationDialog(place) },
            onEditListener = { place -> showEditDialog(place) }
        )
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

    private fun onPlaceDeleteClicked(place: SavedPlace) {
        Thread {
            dao.delete(place.id)
            val updatedPlaces = dao.findAll()
            runOnUiThread {
                adapter.updatePlaces(updatedPlaces)
                runOnUiThread {
                    adapter.updatePlaces(updatedPlaces)
                    Toast.makeText(this, "Lugar eliminado", Toast.LENGTH_SHORT).show()
                }

            }
        }.start()
    }

    private fun onPlaceEditClicked(updatedPlace: SavedPlace) {
        Thread {
            dao.update(updatedPlace)
            val updatedPlaces = dao.findAll()
            runOnUiThread {
                adapter.updatePlaces(updatedPlaces)
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

    private fun showEditDialog(place: SavedPlace) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_place, null)
        val etName = dialogView.findViewById<android.widget.EditText>(R.id.etName)
        val etLatitude = dialogView.findViewById<android.widget.EditText>(R.id.etLatitude)
        val etLongitude = dialogView.findViewById<android.widget.EditText>(R.id.etLongitude)

        // Mostrar los valores actuales
        etName.setText(place.name)
        etLatitude.setText(place.latitude.toString())
        etLongitude.setText(place.longitude.toString())

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Editar lugar")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                // Crear un nuevo objeto con los valores actualizados
                val updatedPlace = place.copy(
                    name = etName.text.toString(),
                    latitude = etLatitude.text.toString().toDoubleOrNull() ?: place.latitude,
                    longitude = etLongitude.text.toString().toDoubleOrNull() ?: place.longitude
                )

                // Guardar los cambios en la BD
                onPlaceEditClicked(updatedPlace)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(place: SavedPlace) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Eliminar lugar")
            .setMessage("¿Seguro que deseas eliminar \"${place.name}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                // Si el usuario confirma, eliminamos el lugar
                onPlaceDeleteClicked(place)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }





}
