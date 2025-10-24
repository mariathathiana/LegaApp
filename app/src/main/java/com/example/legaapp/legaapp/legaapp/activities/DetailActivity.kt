package com.example.legaapp.legaapp.legaapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.legaapp.legaapp.legaapp.R
import com.example.legaapp.legaapp.legaapp.data.Lega
import com.example.legaapp.legaapp.legaapp.databinding.ActivityDetailBinding
import com.example.legaapp.legaapp.legaapp.utils.SessionManager

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var session: SessionManager
    private var favoriteMenu: MenuItem? = null
    private var isFavorite = false

    private var lega: Lega? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)

        // Obtener ID seguro
        val id = intent.getStringExtra("LEGA_ID")
        if (id.isNullOrEmpty()) {
            Toast.makeText(this, "No se recibió el ID del lugar", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Obtener Lega
        lega = try {
            Lega.getById(id)
        } catch (e: NoSuchElementException) {
            Log.e("DetailActivity", "Lega con id $id no encontrado")
            Toast.makeText(this, "Lugar no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Configurar favorite
        isFavorite = session.isFavorite(id)

        // Mostrar datos del lugar
        binding.nameTextView.text = getLegaName(lega!!)
        binding.iconImageView.setImageResource(lega!!.sign)

        binding.btnOpenMap.setOnClickListener {
            openMap()
        }


        // ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getLegaName(lega!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_detail_menu, menu)
        favoriteMenu = menu.findItem(R.id.action_favorite)
        setFavoriteMenu()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                lega?.let {
                    isFavorite = !isFavorite
                    if (isFavorite) session.setFavorite(it.id)
                    else session.setFavorite("")
                    setFavoriteMenu()
                }
                true
            }
            R.id.action_share -> {
                Toast.makeText(this, "Compartir lugar", Toast.LENGTH_SHORT).show()
                Log.i("MENU", "He pulsado el menu de compartir")
                true
            }
            R.id.action_map -> {
                openMap()
                true
            }

            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setFavoriteMenu() {
        favoriteMenu?.setIcon(
            if (isFavorite) R.drawable.ic_favorite_selected
            else R.drawable.ic_favorite
        )
    }

    // Función segura para obtener el nombre del lugar
    private fun getLegaName(lega: Lega): String {
        return try {
            getString(lega.name) // Si es resource ID
        } catch (e: Exception) {
            lega.name.toString() // Si es literal
        }

    }

    private fun openMap() {
        lega?.let { l ->
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("LATITUDE", l.latitude)
            intent.putExtra("LONGITUDE", l.longitude)
            intent.putExtra("TITLE", getLegaName(l))
            startActivity(intent)
        } ?: run {
            Toast.makeText(this, "No hay lugar seleccionado", Toast.LENGTH_SHORT).show()
        }
    }


}
