package com.ubaya.project_uts_160420147.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.project_uts_160420147.R
import org.json.JSONException
import org.json.JSONObject

class LoginFragment : Fragment() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var txtBuatAkun : TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        editTextUsername = view.findViewById(R.id.editTextUsername)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        buttonLogin = view.findViewById(R.id.buttonLogin)
        txtBuatAkun = view.findViewById(R.id.txtBuatAkun)

        buttonLogin.setOnClickListener {
            login()
        }
        txtBuatAkun.setOnClickListener {
            navigateToRegisterFragment()
        }




        return view
    }
    private fun navigateToRegisterFragment() {
        // Use findNavController to perform navigation action
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun login() {
        val queue = Volley.newRequestQueue(context)
        val url = "http://192.168.1.80/anmp_uts/login.php"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.getString("status")
                    if (status == "success") {
                        val userData = jsonObject.getJSONObject("userData")
                        saveUserData(userData)
                        findNavController().navigate(R.id.action_loginFragment_to_itemHome)
                        Toast.makeText(context, "Login successful.", Toast.LENGTH_SHORT).show()
                    } else {
                        val message = jsonObject.getString("message")
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(context, "JSON parsing error: " + e.message, Toast.LENGTH_SHORT).show()
                    Log.e("LoginFragment", "JSON parsing error: " + e.message)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, "Volley error: " + error.message, Toast.LENGTH_SHORT).show()
                Log.e("LoginFragment", "Volley error: " + error.message)
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = editTextUsername.text.toString()
                params["password"] =  editTextPassword.text.toString()
                return params
            }


        }
        queue.add(stringRequest)
    }

    private fun saveUserData(userData: JSONObject) {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("username", userData.getString("username"))
            putString("first_name", userData.getString("FirstName"))
            putString("last_name", userData.getString("LastName"))
            apply()
        }
        Log.d("LoginFragment", "User data saved successfully: Username: ${userData.getString("username")}")
    }
}
