package com.example.legaapp.legaapp.legaapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.legaapp.legaapp.legaapp.R
import android.widget.TextView
import com.example.legaapp.legaapp.legaapp.activities.MapActivity
import com.example.legaapp.legaapp.legaapp.data.SavedPlace


class SavedPlacesAdapter(
    private var items: List<SavedPlace>,
    private val onItemClick: (SavedPlace) -> Unit
) : RecyclerView.Adapter<SavedPlacesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(place: SavedPlace) {
            itemView.findViewById<TextView>(R.id.tvPlaceName).text = place.name
            itemView.findViewById<TextView>(R.id.tvCoordinates).text = "Lat: ${place.latitude}, Lon: ${place.longitude}"
            itemView.setOnClickListener {
                onItemClick(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_save_place, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updatePlaces(newPlaces: List<SavedPlace>) {
        this.items = newPlaces
        notifyDataSetChanged()
    }
}


