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

class MainActivity : AppCompatActivity() {

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
    }

    override fun onResume() {
        super.onResume()
        adapter.updateItems(legaList)
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
                    getString(it.name).search(newText)

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

    private fun onItemClickListener(position: Int) {
        val lega = legaList[position]
        goToDetail(lega)
    }

    private fun goToDetail(lega: Lega) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("LEGA_ID", lega.id)
        startActivity(intent)
    }
}
