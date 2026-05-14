package com.example.nammahasiru.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.nammahasiru.R

class GuideFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_guide, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etPlace = view.findViewById<EditText>(R.id.et_place)
        val btnFind = view.findViewById<Button>(R.id.btn_find_trees)
        val llResults = view.findViewById<LinearLayout>(R.id.ll_results)

        btnFind.setOnClickListener {
            val place = etPlace.text.toString().trim()
            if (place.isEmpty()) {
                etPlace.error = "Enter a place"; return@setOnClickListener
            }
            val trees = getTreesForPlace(place)
            llResults.removeAllViews()
            llResults.visibility = View.VISIBLE

            val header = TextView(requireContext()).apply {
                text = "Best trees for ${place.replaceFirstChar { it.uppercase() }}:"
                textSize = 16f
                setTextColor(android.graphics.Color.parseColor("#1B5E20"))
                setTypeface(null, android.graphics.Typeface.BOLD)
                setPadding(0, 0, 0, 16)
            }
            llResults.addView(header)

            trees.forEach { (name, desc) -> llResults.addView(makeCard(name, desc)) }
        }
    }

    private fun makeCard(name: String, desc: String): View {
        val ctx = requireContext()
        val card = androidx.cardview.widget.CardView(ctx).apply {
            radius = 24f
            setCardBackgroundColor(android.graphics.Color.parseColor("#E8F5E9"))
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 24 }
            layoutParams = lp
        }
        val inner = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 32, 40, 32)
        }
        inner.addView(TextView(ctx).apply {
            text = name; textSize = 16f; setTypeface(null, android.graphics.Typeface.BOLD)
            setTextColor(android.graphics.Color.parseColor("#2E7D32"))
        })
        inner.addView(TextView(ctx).apply {
            text = desc; textSize = 13f
            setTextColor(android.graphics.Color.parseColor("#555555"))
            setPadding(0, 8, 0, 0)
        })
        card.addView(inner)
        return card
    }

    private fun getTreesForPlace(place: String): List<Pair<String, String>> {
        val p = place.lowercase()
        return when {
            anyOf(p, "kerala", "trivandrum", "kochi", "kozhikode", "thrissur") -> listOf(
                "🥥 Coconut (Cocos nucifera)" to "Ideal for Kerala's humid coastal climate. Multipurpose — food, oil, fiber. Thrives in sandy coastal soil.",
                "🍈 Jackfruit (Artocarpus heterophyllus)" to "State fruit of Kerala. Massive yield, great shade. Loves humid tropical weather.",
                "🌺 Indian Tulip (Thespesia populnea)" to "Salt-tolerant, beautiful flowers. Perfect for backwaters & coastal areas.",
                "🌿 Neem (Azadirachta indica)" to "Medicinal, fast-growing, drought-tolerant. Good for inland Kerala.",
                "🎋 Bamboo (Bambusoideae)" to "Excellent for river banks and hilly areas in Kerala. Fast-growing erosion control."
            )

            anyOf(
                p,
                "karnataka",
                "bengaluru",
                "bangalore",
                "mysuru",
                "mysore",
                "mangaluru",
                "hubli"
            ) -> listOf(
                "🌿 Neem (Azadirachta indica)" to "Thrives across Karnataka. Pest-resistant, great urban tree for Bengaluru.",
                "🌳 Peepal (Ficus religiosa)" to "Adaptable to all Karnataka soils. Sacred & oxygen-rich. Ideal for roadsides.",
                "🌾 Honge (Millettia pinnata)" to "Karnataka's own roadside champion. Drought-resistant, nitrogen-fixing, biofuel seeds.",
                "🌲 Banyan (Ficus benghalensis)" to "Massive canopy, long-lived. Perfect for parks and open spaces in Mysuru & Bengaluru.",
                "🌺 Indian Tulip (Thespesia populnea)" to "Best for coastal Mangaluru & Udupi. Salt-tolerant, attractive flowers."
            )

            anyOf(
                p,
                "tamil nadu",
                "tamilnadu",
                "chennai",
                "madurai",
                "coimbatore",
                "trichy"
            ) -> listOf(
                "🌿 Neem (Azadirachta indica)" to "Extremely suited to Tamil Nadu's hot climate. Medicinal & shade-giving.",
                "🍫 Tamarind (Tamarindus indica)" to "Iconic Tamil Nadu tree. Drought-tolerant, massive canopy, culinary importance.",
                "🌳 Peepal (Ficus religiosa)" to "Sacred & hardy. Common across Tamil Nadu temples and roadsides.",
                "🥭 Mango (Mangifera indica)" to "Fruit orchards thrive across Tamil Nadu especially in Salem, Krishnagiri.",
                "🌾 Honge/Karanj (Millettia pinnata)" to "Excellent roadside tree for coastal Tamil Nadu. Salt & drought tolerant."
            )

            anyOf(p, "maharashtra", "mumbai", "pune", "nagpur", "nashik", "aurangabad") -> listOf(
                "🌸 Gulmohar (Delonix regia)" to "Mumbai's iconic street tree. Brilliant red flowers, fast-growing, good shade.",
                "🌿 Neem (Azadirachta indica)" to "Thrives across Maharashtra's varied terrain — coast to Vidarbha plateau.",
                "🥭 Mango (Mangifera indica)" to "Alphonso belt in Konkan. Excellent for Western Maharashtra orchards.",
                "🪵 Teak (Tectona grandis)" to "Premium timber tree. Grows well in Vidarbha and Marathwada regions.",
                "🫐 Amla (Phyllanthus emblica)" to "Hardy & medicinal. Excellent for drylands of Marathwada and Aurangabad."
            )

            anyOf(p, "rajasthan", "jaipur", "jodhpur", "udaipur", "bikaner", "jaisalmer") -> listOf(
                "🌿 Neem (Azadirachta indica)" to "Rajasthan's most important tree. Survives extreme heat and low water.",
                "🍫 Khejri (Prosopis cineraria)" to "State tree of Rajasthan. Extremely drought tolerant. Sacred to Bishnoi community.",
                "🌵 Rohida (Tecomella undulata)" to "State flower tree of Rajasthan. Adapted to desert, beautiful orange flowers.",
                "🌾 Ber (Ziziphus mauritiana)" to "Common across Rajasthan. Edible fruit, thorny hedge, very drought-hardy.",
                "🫐 Amla (Phyllanthus emblica)" to "Thrives in hot, dry Rajasthan climate. Excellent for nutrition and Ayurveda."
            )

            anyOf(p, "gujarat", "ahmedabad", "surat", "vadodara", "rajkot") -> listOf(
                "🌿 Neem (Azadirachta indica)" to "Ideal for Gujarat's semi-arid climate. Common urban tree.",
                "🌳 Banyan (Ficus benghalensis)" to "Gujarat's famous Kabir Vad is a massive banyan. Excellent for open areas.",
                "🥭 Mango (Mangifera indica)" to "Kesar mango of Gir Somnath is world-famous. Great for southern Gujarat.",
                "🌾 Honge/Karanj (Millettia pinnata)" to "Coastal Gujarat loves this salt-tolerant tree for roadsides.",
                "🪵 Teak (Tectona grandis)" to "Good for South Gujarat where rainfall is higher. Quality timber."
            )

            anyOf(p, "madhya pradesh", "mp", "bhopal", "indore", "gwalior", "jabalpur") -> listOf(
                "🪵 Teak (Tectona grandis)" to "MP has India's largest teak forests. Excellent for plantations.",
                "🎋 Bamboo (Bambusoideae)" to "Abundant in MP's tribal forest regions. Fast-growing and multipurpose.",
                "🌿 Neem (Azadirachta indica)" to "Thrives in MP's tropical climate. Medicinal and shade value.",
                "🫐 Amla (Phyllanthus emblica)" to "Major Amla-producing state. Great for dryland orchards in MP.",
                "🌳 Peepal (Ficus religiosa)" to "Sacred and adaptable. Common across MP towns and villages."
            )

            anyOf(
                p,
                "uttar pradesh",
                "up",
                "lucknow",
                "varanasi",
                "agra",
                "kanpur",
                "allahabad",
                "prayagraj"
            ) -> listOf(
                "🥭 Mango (Mangifera indica)" to "UP is India's top mango state. Dasheri & Langra varieties are world-famous.",
                "🌿 Neem (Azadirachta indica)" to "Common and thriving across UP's plains. Important village tree.",
                "🌳 Peepal (Ficus religiosa)" to "Sacred along the Ganges. Thrives in UP's alluvial plains.",
                "🌸 Gulmohar (Delonix regia)" to "Popular urban tree in Lucknow, Agra and Kanpur for shade and beauty.",
                "🫐 Amla (Phyllanthus emblica)" to "Grown extensively in Pratapgarh district. Excellent for UP's dry belt."
            )

            anyOf(p, "punjab", "haryana", "chandigarh", "ludhiana", "amritsar") -> listOf(
                "🌳 Shisham (Dalbergia sissoo)" to "State tree of Punjab. Excellent timber, hardy, common along canals & roadsides.",
                "🌿 Neem (Azadirachta indica)" to "Hardy & fast-growing. Excellent for Punjab's hot summers.",
                "🍫 Mulberry (Morus alba)" to "Common in Punjab. Supports silkworm industry, edible fruit.",
                "🌸 Gulmohar (Delonix regia)" to "Beautiful urban tree for Chandigarh and Ludhiana cityscapes.",
                "🪵 Eucalyptus (Eucalyptus globulus)" to "Very fast-growing in Punjab's plains for timber (use carefully — water intensive)."
            )

            anyOf(p, "himachal", "shimla", "manali", "dharamsala", "kullu", "mandi") -> listOf(
                "🌲 Deodar Cedar (Cedrus deodara)" to "State tree of HP. Sacred, majestic, thrives at high altitudes (1500–3000m).",
                "🍎 Apple (Malus domestica)" to "HP is India's apple capital. Perfect climate in Shimla & Kullu valleys.",
                "🌲 Oak (Quercus spp.)" to "Common in HP's mid-altitude forests. Essential for watershed conservation.",
                "🍑 Rhododendron (Rhododendron arboreum)" to "State flower tree. Beautiful red flowers in spring. Thrives above 1500m.",
                "🎋 Bamboo (Bambusoideae)" to "Lower Himachal & valley floors. Good for erosion control."
            )

            anyOf(
                p,
                "uttarakhand",
                "dehradun",
                "haridwar",
                "nainital",
                "mussoorie",
                "rishikesh"
            ) -> listOf(
                "🌲 Sal (Shorea robusta)" to "Dominant forest tree of Uttarakhand's lower ranges. Important for ecology.",
                "🌲 Deodar Cedar (Cedrus deodara)" to "State tree. Grows in higher Garhwal & Kumaon ranges.",
                "🍑 Rhododendron (Rhododendron arboreum)" to "State flower. Thrives in Kumaon hills above 1800m.",
                "🌿 Neem (Azadirachta indica)" to "Great for foothills and valleys like Dehradun & Haridwar.",
                "🎋 Bamboo (Bambusoideae)" to "Lower foothills and Terai region. Good for river bank stabilization."
            )

            anyOf(p, "west bengal", "kolkata", "darjeeling", "siliguri", "howrah") -> listOf(
                "🌳 Banyan (Ficus benghalensis)" to "The famous Acharya Jagadish Chandra Bose Botanic Garden banyan is in WB. Thrives in humid climate.",
                "🍈 Jackfruit (Artocarpus heterophyllus)" to "Excellent for WB's humid tropics. State fruit of some nearby regions.",
                "🎋 Bamboo (Bambusoideae)" to "Thrives in North Bengal's foothills and delta region. Multipurpose.",
                "🥭 Mango (Mangifera indica)" to "Himsagar & Langra varieties from WB are very famous.",
                "🌿 Neem (Azadirachta indica)" to "Common village tree across Bengal's tropical plains."
            )

            anyOf(p, "odisha", "bhubaneswar", "cuttack", "puri", "rourkela") -> listOf(
                "🌳 Peepal (Ficus religiosa)" to "Sacred tree in Odisha temples. Thrives in tropical climate.",
                "🎋 Bamboo (Bambusoideae)" to "Abundant in Odisha's tribal belt. Tribal crafts & construction.",
                "🌺 Indian Tulip (Thespesia populnea)" to "Coastal Odisha — salt tolerant, good for Puri & Chilika lake areas.",
                "🌿 Neem (Azadirachta indica)" to "Widely used across Odisha for shade and medicine.",
                "🪵 Sal (Shorea robusta)" to "Dominant forest tree in interior Odisha. Important timber & ecology."
            )

            anyOf(
                p,
                "assam",
                "guwahati",
                "dispur",
                "northeast",
                "meghalaya",
                "manipur",
                "nagaland",
                "tripura",
                "arunachal",
                "mizoram",
                "sikkim"
            ) -> listOf(
                "🎋 Bamboo (Bambusoideae)" to "Northeast India has the richest bamboo diversity in the world. Essential for local economy and ecology.",
                "🍵 Tea (Camellia sinensis)" to "Assam is India's largest tea-producing region. Riverine plains are perfect.",
                "🌲 Hollong (Dipterocarpus macrocarpus)" to "State tree of Assam. Tall tropical rainforest tree. Important timber.",
                "🌿 Neem (Azadirachta indica)" to "Good for lower Assam plains and Brahmaputra valley areas.",
                "🍈 Jackfruit (Artocarpus heterophyllus)" to "Thrives in Northeast's humid tropical climate. Good for home gardens."
            )

            anyOf(
                p,
                "andhra pradesh",
                "ap",
                "hyderabad",
                "vijayawada",
                "visakhapatnam",
                "vizag",
                "tirupati"
            ) -> listOf(
                "🌿 Neem (Azadirachta indica)" to "Excellent for AP's hot semi-arid climate. Common village & roadside tree.",
                "🍫 Tamarind (Tamarindus indica)" to "Very common in Rayalaseema & Telangana border. Iconic AP tree.",
                "🌾 Honge/Karanj (Millettia pinnata)" to "Excellent for AP's coast. Salt-tolerant, nitrogen-fixing.",
                "🥭 Mango (Mangifera indica)" to "Banganapalle & Totapuri varieties from AP are world-famous.",
                "🌳 Peepal (Ficus religiosa)" to "Sacred tree, thrives in AP's tropical climate near temples."
            )

            anyOf(p, "telangana", "hyderabad", "warangal", "karimnagar", "nizamabad") -> listOf(
                "🌿 Neem (Azadirachta indica)" to "Best tree for Telangana's hot climate. Rapid growth, medicinal value.",
                "🍫 Tamarind (Tamarindus indica)" to "Classic Telangana tree. Drought-tolerant, long-lived, culinary importance.",
                "🌳 Banyan (Ficus benghalensis)" to "State tree of Telangana. Iconic for villages, parks and panchayats.",
                "🌾 Honge (Millettia pinnata)" to "Excellent roadside tree for Telangana's dry Deccan plateau.",
                "🪵 Teak (Tectona grandis)" to "Grows in wetter parts of Telangana. Adilabad has natural teak forests."
            )

            anyOf(p, "goa") -> listOf(
                "🥥 Coconut (Cocos nucifera)" to "Goa's most iconic tree. Thrives everywhere in this coastal state.",
                "🌺 Indian Tulip (Thespesia populnea)" to "Coastal Goa — excellent salt-tolerant ornamental tree.",
                "🍈 Jackfruit (Artocarpus heterophyllus)" to "Thrives in Goa's humid tropics. Common in village gardens.",
                "🌸 Gulmohar (Delonix regia)" to "Beautiful urban tree for Panaji and coastal towns.",
                "🥭 Mango (Mangifera indica)" to "Goa's Mankurad & Alfonso mangoes are famous. Great for home orchards."
            )

            anyOf(p, "jammu", "kashmir", "srinagar", "leh", "ladakh") -> listOf(
                "🍎 Apple (Malus domestica)" to "Kashmir is famous for apples. Excellent in valley areas 1500–2500m.",
                "🌲 Chinar (Platanus orientalis)" to "Kashmir's iconic tree. Magnificent autumn colors. State tree of J&K.",
                "🌲 Deodar Cedar (Cedrus deodara)" to "Sacred and majestic. Thrives in Jammu's higher elevation ranges.",
                "🫐 Walnut (Juglans regia)" to "Kashmir walnuts are world-class. Excellent for 1000–2000m altitude.",
                "🍑 Cherry (Prunus avium)" to "Thrives in Kashmir's cool temperate climate. Beautiful spring blooms."
            )

            else -> listOf(
                "🌿 Neem (Azadirachta indica)" to "Hardy, drought-tolerant, medicinal. Grows across nearly all of India.",
                "🌳 Peepal (Ficus religiosa)" to "Sacred, adaptable to any Indian soil. Excellent oxygen producer.",
                "🌲 Banyan (Ficus benghalensis)" to "National tree of India. Massive, long-lived, great for shade & ecology.",
                "🥭 Mango (Mangifera indica)" to "National fruit tree. Grows in most tropical & subtropical Indian regions.",
                "🎋 Bamboo (Bambusoideae)" to "Fastest growing plant. Multipurpose and found across India."
            )
        }
    }

    private fun anyOf(place: String, vararg keywords: String) = keywords.any { it in place }
}