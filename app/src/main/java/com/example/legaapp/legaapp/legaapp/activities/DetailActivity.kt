package com.example.legaapp.legaapp.legaapp.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
    private lateinit var favoriteMenu: MenuItem
    private var isFavorite = false

    private lateinit var lega: Lega

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)

        val id = intent.getStringExtra("LEGA_ID")



        isFavorite = session.isFavorite(id)

        lega = Lega.getById(id)

        // Aquí accedes directo por binding a los views
        binding.nameTextView.text = getString(lega.name)

        binding.iconImageView.setImageResource(lega.sign)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(lega.name)
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
                isFavorite = !isFavorite
                if (isFavorite) {
                    session.setFavorite(lega.id)
                } else {
                    session.setFavorite("")
                }
                setFavoriteMenu()
                true
            }
            R.id.action_share -> {
                Log.i("MENU", "He pulsado el menu de compartir")
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
        if (isFavorite) {
            favoriteMenu.setIcon(R.drawable.ic_favorite_selected)
        } else {
            favoriteMenu.setIcon(R.drawable.ic_favorite)
        }
    }
}
