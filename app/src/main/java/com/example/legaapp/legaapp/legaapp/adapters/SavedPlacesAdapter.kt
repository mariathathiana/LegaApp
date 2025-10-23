package com.example.legaapp.legaapp.legaapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.legaapp.legaapp.legaapp.data.SavedPlace
import com.example.legaapp.legaapp.legaapp.databinding.ItemSavePlaceBinding

class SavedPlacesAdapter(
    private var items: List<SavedPlace>,
    private val onItemClick: (SavedPlace) -> Unit
) : RecyclerView.Adapter<SavedPlacesAdapter.ViewHolder>(), Filterable {

    private var filteredItems: List<SavedPlace> = items.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavePlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredItems[position])
    }

    fun updatePlaces(newPlaces: List<SavedPlace>) {
        this.items = newPlaces
        this.filteredItems = newPlaces.toList()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredItems = if (charString.isEmpty()) {
                    items
                } else {
                    items.filter {
                        it.name.contains(charString, ignoreCase = true)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredItems
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems = results?.values as List<SavedPlace>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(private val binding: ItemSavePlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: SavedPlace) {
            binding.tvPlaceName.text = place.name
            binding.tvCoordinates.text = "Lat: ${place.latitude}, Lon: ${place.longitude}"
            binding.root.setOnClickListener {
                onItemClick(place)
            }
        }
    }
}



