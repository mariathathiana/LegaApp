package com.example.legaapp.legaapp.legaapp.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.legaapp.R
import com.example.legaapp.legaapp.legaapp.data.Lega
import com.example.legaapp.legaapp.legaapp.utils.SessionManager

class DetailActivity : AppCompatActivity() {lateinit var nameTextView: TextView
    lateinit var datesTextView: TextView
    lateinit var iconImageView: ImageView

    lateinit var Lega : Lega
    lateinit var session: SessionManager
    lateinit var favoriteMenu: MenuItem
    var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)

        nameTextView = findViewById(R.id.nameTextView)
        datesTextView = findViewById(R.id.datesTextView)
        iconImageView = findViewById(R.id.iconImageView)

        val id = intent.getStringExtra("LEGA_ID")!!

        isFavorite = session.isFavorite(id)

        lega = Lega.getById(id)

        nameTextView.setText(lega.name)

        iconImageView.setImageResource(lega.sign)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(lega.name)

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
                    session.setFavorite(legaid)
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

    fun setFavoriteMenu() {
        if (isFavorite) {
            favoriteMenu.setIcon(R.drawable.ic_favorite_selected)
        } else {
            favoriteMenu.setIcon(R.drawable.ic_favorite)
        }
    }
}