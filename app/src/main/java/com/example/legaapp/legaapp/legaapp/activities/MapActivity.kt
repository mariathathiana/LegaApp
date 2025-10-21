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
import com.example.legaapp.legaapp.legaapp.R
import android.view.MotionEvent
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.MapEventsOverlay




class MapActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var marker: Marker
    private var selectedPoint: GeoPoint? = null

    private val savedPlaces = mutableListOf<SavedPlace>()

    data class SavedPlace(val name: String, val latitude: Double, val longitude: Double)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Botón "Up"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Mapa"

        Configuration.getInstance().load(applicationContext,
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))

        map = findViewById(R.id.mapView)
        map.setMultiTouchControls(true)

        val lat = intent.getDoubleExtra("LATITUDE", 0.0)
        val lon = intent.getDoubleExtra("LONGITUDE", 0.0)
        val title = intent.getStringExtra("TITLE") ?: "Ubicación"

        val point = GeoPoint(lat, lon)
        map.controller.setZoom(15.0)
        map.controller.setCenter(point)

        marker = Marker(map)
        marker.position = point
        marker.title = title
        map.overlays.add(marker)


        // Listener para seleccionar lugar en mapa
        map.overlays.add(object : Overlay() {
            override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
                if (e != null && mapView != null) {
                    val projection = mapView.projection
                    val geoPoint = projection.fromPixels(e.x.toInt(), e.y.toInt())
                    selectedPoint = geoPoint as GeoPoint?

                    // Mueve el marcador
                    marker.position = geoPoint
                    marker.title = "Lugar seleccionado"
                    map.overlays.clear()
                    map.overlays.add(marker)
                    map.invalidate()

                    // Muestra el diálogo para ingresar el nombre
                    showNameInputDialog()
                    return true
                }
                return false
            }
        })

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
        savedPlaces.add(SavedPlace(name, point.latitude, point.longitude))
        Toast.makeText(this, "Lugar guardado: $name", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
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

