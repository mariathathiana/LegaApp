package com.example.legaapp.legaapp.legaapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.legaapp.legaapp.legaapp.R
import com.example.legaapp.legaapp.legaapp.adapters.LegaAdapter
import com.example.legaapp.legaapp.legaapp.data.Lega
import com.example.legaapp.legaapp.legaapp.databinding.ActivityMainBinding
import com.example.legaapp.legaapp.legaapp.utils.search
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : AppCompatActivity() {

    private lateinit var map: MapView


    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LegaAdapter

    private var legaList: List<Lega> = Lega.getAll()
    private var isGridViewEnabled = false
    private lateinit var viewModeMenu: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setTitle(R.string.activity_main_title)

        setupViewMode()

        // Cargar configuración de osmdroid
        Configuration.getInstance().load(applicationContext, androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))

        map = binding.map
        map.setMultiTouchControls(true)

        // Centrar el mapa en una ubicación (ejemplo Buenos Aires)
        val startPoint = GeoPoint(-34.6037, -58.3816)
        map.controller.setZoom(12.0)
        map.controller.setCenter(startPoint)

        // Añadir marcador de ejemplo
        val marker = Marker(map)
        marker.position = startPoint
        marker.title = "Hola desde OpenStreetMap!"
        map.overlays.add(marker)



    }



    override fun onResume() {
        super.onResume()
        map.onResume()
        adapter.updateItems(legaList)
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)

        viewModeMenu = menu.findItem(R.id.action_view_mode)
        setViewModeMenu()

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                legaList = Lega.getAll().filter {
                    getString(it.name).contains(newText, ignoreCase = true)

                }
                adapter.updateItems(legaList)
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_view_mode -> {
                isGridViewEnabled = !isGridViewEnabled
                setupViewMode()
                setViewModeMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViewMode() {
        if (isGridViewEnabled) {
            adapter = LegaAdapter(legaList, ::onItemClickListener, R.layout.item_lega_grid)
            binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        } else {
            adapter = LegaAdapter(legaList, ::onItemClickListener, R.layout.item_lega_list)
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setViewModeMenu() {
        if (isGridViewEnabled) {
            viewModeMenu.setIcon(R.drawable.ic_list_view)
        } else {
            viewModeMenu.setIcon(R.drawable.ic_grid_view)
        }
    }



    private fun goToDetail(lega: Lega) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("LEGA_ID", lega.id)
        startActivity(intent)
    }

    private fun onItemClickListener(position: Int) {
        val lega = legaList[position]
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("LATITUDE", lega.latitude)
        intent.putExtra("LONGITUDE", lega.longitude)
        intent.putExtra("TITLE", lega.name)
        startActivity(intent)
        goToDetail(lega)
    }

}
