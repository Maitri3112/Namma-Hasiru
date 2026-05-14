package com.example.nammahasiru.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nammahasiru.R
import com.example.nammahasiru.data.PlantDatabase

class AllPlantsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_all_plants, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = view.findViewById<RecyclerView>(R.id.rv_all_plants)
        rv.layoutManager = LinearLayoutManager(requireContext())
        PlantDatabase.getDatabase(requireContext()).plantDao()
            .getAllPlants().observe(viewLifecycleOwner) { plants ->
                rv.adapter = PlantAdapter(plants)
            }
    }
}
