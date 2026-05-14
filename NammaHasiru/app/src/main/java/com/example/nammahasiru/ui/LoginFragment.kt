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

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etUsername = view.findViewById<EditText>(R.id.et_username)
        val etPassword = view.findViewById<EditText>(R.id.et_password)
        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val tvError = view.findViewById<TextView>(R.id.tv_error)
        val tvRegister = view.findViewById<TextView>(R.id.tv_go_register)
        val dao = PlantDatabase.getDatabase(requireContext()).userDao()

        tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btnLogin.setOnClickListener {
            val user = etUsername.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                tvError.visibility = View.VISIBLE
                tvError.text = "❌ Enter username and password"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val found = dao.login(user, pass)
                requireActivity().runOnUiThread {
                    if (found != null) {
                        // Save logged-in username to SharedPreferences
                        requireContext()
                            .getSharedPreferences("nh_prefs", Context.MODE_PRIVATE)
                            .edit().putString("logged_user", found.username)
                            .putString("logged_name", found.fullName)
                            .apply()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        tvError.visibility = View.VISIBLE
                        tvError.text = "❌ Invalid username or password"
                    }
                }
            }
        }
    }
}