

package com.example.nammahasiru.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.example.nammahasiru.R
import com.example.nammahasiru.data.Plant
import com.example.nammahasiru.data.PlantDatabase
import com.example.nammahasiru.worker.ReminderWorker
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AddPlantFragment : Fragment() {

    private var lat = 0.0
    private var lon = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_add_plant, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etSpecies = view.findViewById<EditText>(R.id.et_species)
        val etVillage = view.findViewById<EditText>(R.id.et_village)
        val etDays = view.findViewById<EditText>(R.id.et_reminder_days)
        val tvLoc = view.findViewById<TextView>(R.id.tv_location)
        val rgStatus = view.findViewById<RadioGroup>(R.id.rg_status)
        val fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val db = PlantDatabase.getDatabase(requireContext())

        view.findViewById<Button>(R.id.btn_get_location).setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
                return@setOnClickListener
            }
            fusedClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    lat = loc.latitude; lon = loc.longitude
                    tvLoc.text = "📍 Lat: %.4f, Lon: %.4f".format(lat, lon)
                } else tvLoc.text = "Could not get location. Try outdoors."
            }
        }

        view.findViewById<Button>(R.id.btn_save).setOnClickListener {
            val species = etSpecies.text.toString().trim()
            val village = etVillage.text.toString().trim()
            val daysStr = etDays.text.toString().trim()

            if (species.isEmpty()) {
                etSpecies.error = "Enter species name"; return@setOnClickListener
            }
            if (lat == 0.0) {
                Toast.makeText(requireContext(), "Get location first!", Toast.LENGTH_SHORT)
                    .show(); return@setOnClickListener
            }
            if (daysStr.isEmpty()) {
                etDays.error = "Enter reminder days"; return@setOnClickListener
            }

            val days = daysStr.toLongOrNull() ?: 90L
            val status = when (rgStatus.checkedRadioButtonId) {
                R.id.rb_sprouted -> "Sprouted"
                R.id.rb_died -> "Died"
                else -> "Planted"
            }

            val loggedUser = requireContext()
                .getSharedPreferences("nh_prefs", Context.MODE_PRIVATE)
                .getString("logged_user", "") ?: ""

            val plant = Plant(
                speciesName = species, latitude = lat, longitude = lon,
                status = status, village = village
            )

            lifecycleScope.launch {
                db.plantDao().insert(plant)
                if (loggedUser.isNotEmpty()) db.userDao().incrementPlantCount(loggedUser)

                val workReq = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(days, TimeUnit.DAYS)
                    .setInputData(workDataOf("species" to species, "days" to days))
                    .build()
                WorkManager.getInstance(requireContext()).enqueue(workReq)

                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "🌱 Saved! Reminder set for $days days.", Toast.LENGTH_LONG
                    ).show()
                    etSpecies.text?.clear(); etVillage.text?.clear(); etDays.text?.clear()
                    tvLoc.text = "No location yet"; lat = 0.0; lon = 0.0
                }
            }
        }
    }
}