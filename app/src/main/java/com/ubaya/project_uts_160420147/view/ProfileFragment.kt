package com.ubaya.project_uts_160420147.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.project_uts_160420147.databinding.FragmentProfilBinding
import com.ubaya.project_uts_160420147.viewmodel.ProfileViewModel
import org.json.JSONException
import org.json.JSONObject

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData()

        viewModel.firstName.observe(viewLifecycleOwner) { firstName ->
            binding.editTextFirstName.setText(firstName)
            Log.d("ProfileFragment", "First Name loaded: $firstName")
        }
        viewModel.lastName.observe(viewLifecycleOwner) { lastName ->
            binding.editTextLastName.setText(lastName)
            Log.d("ProfileFragment", "Last Name loaded: $lastName")
        }

        binding.buttonSaveChanges.setOnClickListener {
            saveUserData()
            Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            Log.d("ProfileFragment", "Data saved: First Name: ${binding.editTextFirstName.text}, Last Name: ${binding.editTextLastName.text}")
        }

        binding.buttonSignOut.setOnClickListener {
            logout()
            Toast.makeText(context, "Logged out Successfully", Toast.LENGTH_SHORT).show()
            Log.d("ProfileFragment", "User logged out")
        }
    }

    private fun loadUserData() {
        val sharedPreferences = activity?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        binding.editTextFirstName.setText(sharedPreferences?.getString("first_name", ""))
        binding.editTextLastName.setText(sharedPreferences?.getString("last_name", ""))
    }

    private fun saveUserData() {
        val firstName = binding.editTextFirstName.text.toString()
        val lastName = binding.editTextLastName.text.toString()
        val password = binding.editTextPassword.text.toString()

        val sharedPreferences = activity?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val storedFirstName = sharedPreferences?.getString("first_name", "")
        val storedLastName = sharedPreferences?.getString("last_name", "")
        val storedPassword = sharedPreferences?.getString("password", "")

        val queue = Volley.newRequestQueue(context)
        val url = "http://192.168.1.80/anmp_uts/update.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.getString("status")
                    val message = jsonObject.getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    // Refresh UI if changes are successful
                    if (status == "success") {
                        // Update SharedPreferences with new data
                        val editor = sharedPreferences?.edit()
                        if (firstName != storedFirstName) editor?.putString("first_name", firstName)
                        if (lastName != storedLastName) editor?.putString("last_name", lastName)
                        if (password != storedPassword) editor?.putString("password", password)
                        editor?.apply()

                        viewModel.firstName.postValue(firstName)
                        viewModel.lastName.postValue(lastName)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "JSON parsing error: " + e.message, Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, "Volley error: " + error.message, Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = viewModel.username.value ?: ""
                params["firstName"] = firstName
                params["lastName"] = lastName
                params["password"] = password
                return params
            }
        }
        queue.add(stringRequest)
    }




    private fun logout() {
        viewModel.clearUserData()
        findNavController().navigate(ProfileFragmentDirections.actionItemProfileToLoginFragment())
        Log.d("ProfileFragment", "Clearing user data and navigating to login")
    }

}


