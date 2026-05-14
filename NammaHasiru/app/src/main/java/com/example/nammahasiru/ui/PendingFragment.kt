package com.example.nammahasiru.ui

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nammahasiru.R
import com.example.nammahasiru.data.PlantDatabase

class PendingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_pending, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv      = view.findViewById<RecyclerView>(R.id.rv_pending)
        val tvEmpty = view.findViewById<TextView>(R.id.tv_pending_empty)
        rv.layoutManager = LinearLayoutManager(requireContext())

        PlantDatabase.getDatabase(requireContext()).plantDao()
            .getAllPlants().observe(viewLifecycleOwner) { plants ->
                // Pending = status is "Planted" (not yet sprouted or died)
                val pending = plants.filter { it.status == "Planted" }
                if (pending.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    rv.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    rv.visibility = View.VISIBLE
                    rv.adapter = PlantAdapter(pending)
                }
            }
    }
}