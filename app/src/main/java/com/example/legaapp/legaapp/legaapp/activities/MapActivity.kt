package com.example.legaapp.legaapp.legaapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MapEventsOverlay
import com.example.legaapp.legaapp.legaapp.R
import com.example.legaapp.legaapp.legaapp.data.SavedPlace
import com.example.legaapp.legaapp.legaapp.data.SavedPlaceDAO



class MapActivity : AppCompatActivity(), MapEventsOverlay.OnEventListener {

    private lateinit var map: MapView
    private var selectedPoint: GeoPoint? = null

    private lateinit var mapEventsOverlay: MapEventsOverlay
    private var selectionMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Mapa"

        Configuration.getInstance().load(
            applicationContext,
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        map = findViewById(R.id.mapView)
        map.setMultiTouchControls(true)

        val lat = intent.getDoubleExtra("LATITUDE", 0.0)
        val lon = intent.getDoubleExtra("LONGITUDE", 0.0)
        val title = intent.getStringExtra("TITLE") ?: "Ubicación"

        val point = GeoPoint(lat, lon)
        map.controller.setZoom(15.0)
        map.controller.setCenter(point)

        // Añadimos el marcador inicial (si lo hay)
        val initialMarker = Marker(map)
        initialMarker.position = point
        initialMarker.title = title
        map.overlays.add(initialMarker)

        // Crear el overlay que captura taps
        mapEventsOverlay = MapEventsOverlay(object : MapEventsOverlay.OnEventListener {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (p != null) {
                    selectedPoint = p
                    updateSelectionMarker(p)
                    showNameInputDialog()
                    return true
                }
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean = false
        })

        map.overlays.add(mapEventsOverlay)
    }

    private fun updateSelectionMarker(point: GeoPoint) {
        if (selectionMarker == null) {
            selectionMarker = Marker(map)
            map.overlays.add(selectionMarker)
        }
        selectionMarker?.position = point
        selectionMarker?.title = "Lugar seleccionado"
        map.invalidate()
    }

    private fun showNameInputDialog() {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Nombre del lugar")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val placeName = input.text.toString()
                selectedPoint?.let {
                    savePlace(placeName, it)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun savePlace(name: String, point: GeoPoint) {
        val dao = SavedPlaceDAO(this)
        val place = SavedPlace(0, name, point.latitude, point.longitude)

        Thread {
            dao.insert(place)
            runOnUiThread {
                Toast.makeText(
                    this,
                    "Lugar guardado en la base de datos: $name",
                    Toast.LENGTH_SHORT
                ).show()
                loadMarkers()
            }
        }.start()
    }

    private fun loadMarkers() {
        val dao = SavedPlaceDAO(this)
        val places = dao.findAll()

        // Eliminamos solo los marcadores (excepto el selectionMarker)
        val markersToRemove = map.overlays.filterIsInstance<Marker>().filter { it != selectionMarker }
        map.overlays.removeAll(markersToRemove)

        // Añadimos los marcadores guardados
        for (place in places) {
            val marker = Marker(map)
            marker.position = GeoPoint(place.latitude, place.longitude)
            marker.title = place.name
            map.overlays.add(marker)
        }

        map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        loadMarkers()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

