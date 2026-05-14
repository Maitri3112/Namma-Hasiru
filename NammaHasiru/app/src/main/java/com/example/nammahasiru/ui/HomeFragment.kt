package com.example.nammahasiru.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nammahasiru.R
import com.example.nammahasiru.data.Plant
import com.example.nammahasiru.data.PlantDatabase
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = PlantDatabase.getDatabase(requireContext())
        val tvScore = view.findViewById<TextView>(R.id.tv_survival_score)
        val tvStats = view.findViewById<TextView>(R.id.tv_stats)
        val tvLogged = view.findViewById<TextView>(R.id.tv_logged_count)
        val tvPending = view.findViewById<TextView>(R.id.tv_pending_count)
        val rv = view.findViewById<RecyclerView>(R.id.rv_plants)
        val cardLogged = view.findViewById<CardView>(R.id.card_plants_logged)
        val cardPending = view.findViewById<CardView>(R.id.card_pending)
        val etSearch = view.findViewById<EditText>(R.id.et_species_search)
        val btnSearch = view.findViewById<Button>(R.id.btn_species_search)
        val cardResult = view.findViewById<CardView>(R.id.card_species_result)
        val tvResult = view.findViewById<TextView>(R.id.tv_species_result)

        val prefs = requireContext().getSharedPreferences("nh_prefs", Context.MODE_PRIVATE)
        val name = prefs.getString("logged_name", "Friend") ?: "Friend"
        view.findViewById<TextView>(R.id.tv_greeting).text = "🌳 Hello, $name!"

        rv.layoutManager = LinearLayoutManager(requireContext())

        db.plantDao().getAllPlants().observe(viewLifecycleOwner) { plants ->
            // Show only 5 most recent
            rv.adapter = PlantAdapter(plants.take(5))

            lifecycleScope.launch {
                val total = db.plantDao().getTotal()
                val sprouted = db.plantDao().getSprouted()
                val score = if (total > 0) (sprouted * 100) / total else 0
                val pending = plants.count { it.status == "Planted" }

                requireActivity().runOnUiThread {
                    tvScore.text = "$score%"
                    tvStats.text = "$total planted • $sprouted sprouted"
                    tvLogged.text = "$total plants total"
                    tvPending.text = "$pending plants need attention"
                }
            }
        }

        // Navigate to full plant list
        cardLogged.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allPlantsFragment)
        }

        // Navigate to pending checkups
        cardPending.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pendingFragment)
        }

        // Species guide search
        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            if (query.isEmpty()) {
                etSearch.error = "Enter a species name"; return@setOnClickListener
            }
            val info = getSpeciesInfo(query)
            cardResult.visibility = View.VISIBLE
            tvResult.text = info
        }
    }

    private fun getSpeciesInfo(name: String): String {
        val q = name.lowercase()
        return when {
            "neem" in q -> "🌿 Neem (Azadirachta indica)\n\nGrowth: Fast (1–2m/year)\nLifespan: 150–200 years\nWater: Low — drought tolerant\nSoil: Sandy, loamy, well-drained\nClimate: Hot & dry, tropical\nBenefits: Natural pesticide, medicinal leaves & bark, excellent shade tree\nBest regions: All of peninsular India, especially Deccan plateau, Rajasthan"

            "peepal" in q || "bodhi" in q -> "🌳 Peepal (Ficus religiosa)\n\nGrowth: Moderate (0.5–1m/year)\nLifespan: 500–1000+ years\nWater: Moderate\nSoil: Any — very adaptable\nClimate: Tropical & subtropical\nBenefits: Highest O₂ producer, sacred tree, supports wildlife\nBest regions: All of India, especially roadsides & temples"

            "banyan" in q -> "🌲 Banyan (Ficus benghalensis)\n\nGrowth: Moderate\nLifespan: Hundreds of years\nWater: Moderate\nSoil: Deep loam\nClimate: Tropical\nBenefits: Massive canopy for shade, national tree of India, supports ecosystems\nBest regions: All of India — open grounds, parks, villages"

            "honge" in q || "pongam" in q || "karanj" in q -> "🌾 Honge/Karanj (Millettia pinnata)\n\nGrowth: Fast\nLifespan: 80–100 years\nWater: Very low — drought hardy\nSoil: Any — even saline/coastal\nClimate: Tropical, semi-arid\nBenefits: Biofuel seeds, nitrogen-fixing, excellent roadside tree\nBest regions: Karnataka, Maharashtra, Andhra Pradesh, Tamil Nadu"

            "tulip" in q || "thespesia" in q -> "🌺 Indian Tulip (Thespesia populnea)\n\nGrowth: Moderate–Fast\nLifespan: 60–80 years\nWater: Moderate\nSoil: Coastal, clay, sandy\nClimate: Tropical coastal\nBenefits: Salt tolerant, beautiful flowers, good timber\nBest regions: Coastal Karnataka, Kerala, Tamil Nadu, Goa, Odisha"

            "mango" in q -> "🥭 Mango (Mangifera indica)\n\nGrowth: Moderate (0.5m/year)\nLifespan: 100–300 years\nWater: Moderate (drought tolerant once established)\nSoil: Deep loamy, well-drained\nClimate: Tropical & subtropical\nBenefits: Fruit, shade, timber, cultural significance\nBest regions: Maharashtra, UP, Bihar, Andhra Pradesh, Karnataka"

            "coconut" in q -> "🥥 Coconut (Cocos nucifera)\n\nGrowth: Slow to start, then fast\nLifespan: 60–100 years\nWater: Moderate — needs humid climate\nSoil: Sandy coastal, loamy\nClimate: Tropical coastal & humid\nBenefits: Food, oil, fiber, timber, cultural importance\nBest regions: Kerala, coastal Karnataka, Goa, Tamil Nadu, Andhra Pradesh"

            "bamboo" in q -> "🎋 Bamboo (Bambusoideae spp.)\n\nGrowth: Very fast (up to 1m/day for some species)\nLifespan: 30–100 years\nWater: Moderate\nSoil: Well-drained loamy\nClimate: Tropical to temperate\nBenefits: Erosion control, construction material, carbon sequestration\nBest regions: Northeast India, Western Ghats, Odisha, Chhattisgarh"

            "teak" in q -> "🪵 Teak (Tectona grandis)\n\nGrowth: Moderate (1m/year)\nLifespan: 60–80 years\nWater: Moderate (seasonal)\nSoil: Deep, well-drained alluvial\nClimate: Tropical monsoon\nBenefits: Premium timber, very durable wood, good for plantations\nBest regions: Madhya Pradesh, Maharashtra, Karnataka, Kerala, Gujarat"

            "gulmohar" in q || "flamboyant" in q -> "🌸 Gulmohar (Delonix regia)\n\nGrowth: Fast\nLifespan: 40–50 years\nWater: Low–Moderate\nSoil: Sandy, well-drained\nClimate: Tropical\nBenefits: Stunning red flowers, good shade, urban tree\nBest regions: All urban India — Mumbai, Bengaluru, Chennai, Delhi"

            "amla" in q || "gooseberry" in q -> "🫐 Amla (Phyllanthus emblica)\n\nGrowth: Moderate\nLifespan: 50–70 years\nWater: Low–Moderate\nSoil: Loamy to clayey\nClimate: Tropical & subtropical\nBenefits: Superfood fruit, Ayurvedic medicine, very hardy\nBest regions: MP, UP, Rajasthan, Andhra Pradesh, Tamil Nadu"

            "eucalyptus" in q -> "🌿 Eucalyptus (Eucalyptus globulus)\n\nGrowth: Very fast (2–3m/year)\nLifespan: 20–50 years\nWater: Low once established\nSoil: Deep, well-drained\nClimate: Temperate to tropical\nBenefits: Fast biomass, timber, essential oil\n⚠️ Caution: Depletes groundwater, invasive in some regions\nBest regions: Nilgiris, Himachal Pradesh, parts of Karnataka"

            "jackfruit" in q -> "🍈 Jackfruit (Artocarpus heterophyllus)\n\nGrowth: Moderate–Fast\nLifespan: 100 years\nWater: Moderate\nSoil: Deep, well-drained loamy\nClimate: Tropical humid\nBenefits: Large nutritious fruit, timber, shade; state fruit of Kerala\nBest regions: Kerala, Karnataka, Goa, Tamil Nadu, West Bengal"

            "tamarind" in q -> "🍫 Tamarind (Tamarindus indica)\n\nGrowth: Slow\nLifespan: 200+ years\nWater: Very low — drought resistant\nSoil: Sandy, loamy\nClimate: Semi-arid tropical\nBenefits: Food, medicine, carbon storage, excellent shade\nBest regions: Maharashtra, Karnataka, Andhra Pradesh, Tamil Nadu, Telangana"

            else -> "❓ Species \"$name\" not found in our guide.\n\nTry searching for: Neem, Peepal, Banyan, Honge, Mango, Coconut, Bamboo, Teak, Gulmohar, Amla, Tamarind, Jackfruit, Eucalyptus, Tulip, Gulmohar\n\nTip: Check spelling or try the common/local name."
        }
    }
}