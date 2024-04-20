package com.ubaya.project_uts_160420147.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.project_uts_160420147.R
import org.json.JSONException
import org.json.JSONObject

class RegisterFragment : Fragment() {

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var buttonRegister: Button
    private lateinit var buttonBack : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        editTextFirstName = view.findViewById(R.id.editTextFirstName)
        editTextLastName = view.findViewById(R.id.editTextLastName)
        editTextUsername = view.findViewById(R.id.editTextUsername)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        buttonRegister = view.findViewById(R.id.buttonRegister)
        buttonBack = view.findViewById(R.id.btnBackLogin)

        buttonRegister.setOnClickListener {
            registerUser()
        }
        buttonBack.setOnClickListener{
            findNavController().popBackStack()
        }

        return view
    }

    private fun registerUser() {
        val firstName = editTextFirstName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val username = editTextUsername.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        // Validasi dasar untuk memastikan bahwa semua field diisi
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return
        }
        val queue = Volley.newRequestQueue(context)
        val url = "http://192.168.1.80/anmp_uts/register.php"  // Gunakan IP khusus emulator atau konfigurasi server Anda

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()

                    // Jika berhasil, mungkin navigasi ke Login Fragment atau menutup fragment ini
                    if (obj.getString("message") == "User registered successfully") {
                        // Implement navigasi jika diperlukan

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error parsing JSON response", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { volleyError ->
                Toast.makeText(context, "Error: ${volleyError.message}", Toast.LENGTH_LONG).show()
                volleyError.printStackTrace()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["firstName"] = firstName
                params["lastName"] = lastName
                params["username"] = username
                params["email"] = email
                params["password"] = password
                return params
            }
        }

        queue.add(stringRequest)
    }

}
