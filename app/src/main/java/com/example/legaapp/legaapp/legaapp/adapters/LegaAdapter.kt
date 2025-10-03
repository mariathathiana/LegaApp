package com.example.legaapp.legaapp.legaapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.legaapp.legaapp.legaapp.data.Lega
import com.example.legaapp.legaapp.legaapp.utils.SessionManager
import com.example.legaapp.legaapp.legaapp.R

class LegaAdapter(
    var items: List<Lega>,
    val onClickListener: (Int) -> Unit,
    val layout: Int
) : RecyclerView.Adapter<LegaViewHolder>() {

    // Cual es la vista para los elementos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return LegaViewHolder(view)
    }

    // Cuales son los datos para el elemento
    override fun onBindViewHolder(holder: LegaViewHolder, position: Int) {
        val item = items[position]
        holder.render(lega = item)
        holder.itemView.setOnClickListener {
            onClickListener(position)
        }
    }

    // Cuantos elementos se quieren listar?
    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<Lega>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class HoroscopeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    val datesTextView: TextView = view.findViewById(R.id.datesTextView)
    val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
    val favoriteImageView: ImageView = view.findViewById(R.id.favoriteImageView)

    fun render(lega: Lega) {
        nameTextView.setText(lega.name)
        datesTextView.setText(lega.dates)
        iconImageView.setImageResource(lega.sign)

        val session = SessionManager(itemView.context)
        if (session.isFavorite(lega.id)) {
            favoriteImageView.visibility = View.VISIBLE
        } else {
            favoriteImageView.visibility = View.GONE
        }
    }
}