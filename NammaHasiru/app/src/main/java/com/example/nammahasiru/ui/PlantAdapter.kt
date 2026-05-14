package com.example.nammahasiru.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nammahasiru.R
import com.example.nammahasiru.data.Plant
import java.text.SimpleDateFormat
import java.util.*

class PlantAdapter(
    private var plants: List<Plant>,
    private val onItemClick: ((Plant) -> Unit)? = null
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvPlantName)
        val tvSpecies: TextView = itemView.findViewById(R.id.tvPlantSpecies)
        val tvNextWatering: TextView = itemView.findViewById(R.id.tvNextWatering)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plant, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.tvName.text = plant.speciesName
        holder.tvSpecies.text = if (plant.village.isNotEmpty()) plant.village else "—"
        holder.tvNextWatering.text = "Planted: ${sdf.format(Date(plant.plantedDate))}  •  ${plant.status}"
        holder.itemView.setOnClickListener { onItemClick?.invoke(plant) }
    }

    override fun getItemCount(): Int = plants.size

    fun updateList(newPlants: List<Plant>) {
        plants = newPlants
        notifyDataSetChanged()
    }
}