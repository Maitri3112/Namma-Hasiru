package com.example.nammahasiru.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nammahasiru.R
import com.example.nammahasiru.data.PlantDatabase
import com.example.nammahasiru.data.User
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etName     = view.findViewById<EditText>(R.id.et_fullname)
        val etUser     = view.findViewById<EditText>(R.id.et_reg_username)
        val etPass     = view.findViewById<EditText>(R.id.et_reg_password)
        val etConfirm  = view.findViewById<EditText>(R.id.et_reg_confirm)
        val btnReg     = view.findViewById<Button>(R.id.btn_register)
        val tvError    = view.findViewById<TextView>(R.id.tv_reg_error)
        val tvLogin    = view.findViewById<TextView>(R.id.tv_go_login)
        val dao        = PlantDatabase.getDatabase(requireContext()).userDao()

        tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        btnReg.setOnClickListener {
            val name    = etName.text.toString().trim()
            val user    = etUser.text.toString().trim()
            val pass    = etPass.text.toString().trim()
            val confirm = etConfirm.text.toString().trim()

            if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                showError(tvError, "All fields are required"); return@setOnClickListener
            }
            if (pass != confirm) {
                showError(tvError, "Passwords do not match"); return@setOnClickListener
            }
            if (pass.length < 6) {
                showError(tvError, "Password must be at least 6 characters"); return@setOnClickListener
            }

            lifecycleScope.launch {
                val existing = dao.findByUsername(user)
                if (existing != null) {
                    requireActivity().runOnUiThread {
                        showError(tvError, "Username already taken")
                    }
                    return@launch
                }
                dao.insert(User(fullName = name, username = user, password = pass))
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "✅ Account created! Please login.", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }

    private fun showError(tv: TextView, msg: String) {
        tv.visibility = View.VISIBLE
        tv.text = "❌ $msg"
    }
}