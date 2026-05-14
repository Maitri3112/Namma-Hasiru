package com.example.nammahasiru.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nammahasiru.R
import com.example.nammahasiru.data.PlantDatabase
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val prefs    = requireContext().getSharedPreferences("nh_prefs", Context.MODE_PRIVATE)
        val username = prefs.getString("logged_user", "") ?: ""
        val fullName = prefs.getString("logged_name", "User") ?: "User"

        view.findViewById<TextView>(R.id.tv_profile_name).text = fullName
        view.findViewById<TextView>(R.id.tv_profile_username).text = "@$username"

        val tvTotal    = view.findViewById<TextView>(R.id.tv_profile_total)
        val tvSprouted = view.findViewById<TextView>(R.id.tv_profile_sprouted)
        val tvScore    = view.findViewById<TextView>(R.id.tv_profile_score)
        val tvBadge    = view.findViewById<TextView>(R.id.tv_badge)
        val db         = PlantDatabase.getDatabase(requireContext())

        lifecycleScope.launch {
            val total    = db.plantDao().getTotal()
            val sprouted = db.plantDao().getSprouted()
            val score    = if (total > 0) (sprouted * 100) / total else 0
            val badge = when {
                total >= 100 -> "🌳 Master Forester — Incredible! 100+ plants logged!"
                total >= 50  -> "🌲 Grove Guardian — Amazing! 50+ plants!"
                total >= 20  -> "🌿 Green Champion — 20+ plants logged!"
                total >= 10  -> "🍀 Growing Hero — 10+ plants!"
                total >= 5   -> "🌱 Sapling Starter — 5+ plants logged!"
                else         -> "🌱 Seedling — Plant more to level up!"
            }
            requireActivity().runOnUiThread {
                tvTotal.text    = "$total"
                tvSprouted.text = "$sprouted"
                tvScore.text    = "$score%"
                tvBadge.text    = badge
            }
        }

        view.findViewById<Button>(R.id.btn_logout).setOnClickListener {
            prefs.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }
}