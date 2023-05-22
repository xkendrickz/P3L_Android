package com.example.p3l_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_android.api.UserApi
import com.example.p3l_android.databinding.ActivityMainBinding
import com.example.p3l_android.instrukturView.HomeInstrukturActivity
import com.example.p3l_android.memberView.HomeMemberActivity
import com.example.p3l_android.pegawaiView.HomePegawaiActivity
import com.example.p3l_android.models.Auth
import com.example.p3l_android.models.Member
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private val myPreference = "myPref"
    lateinit var mbundle : Bundle
    lateinit var vUsername : String
    lateinit var vPassword : String
//    lateinit var checkUsername : String
//    lateinit var checkPassword : String

    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSupportActionBar()?.hide();
        setTitle("User Login")

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        val textBtnGuest : TextView = findViewById(R.id.textBtnGuest)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        getBundle()
        setText()

        textBtnGuest.setOnClickListener {
            val moveRegister = Intent(this@MainActivity, GuestActivity::class.java)
            startActivity(moveRegister)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false

            if (inputUsername.getEditText()?.getText().toString().isEmpty()) {
                inputUsername.setError("Username must be filled with text")
            }else{
                inputUsername.setError(null)
            }

            if (inputPassword.getEditText()?.getText().toString().isEmpty()) {
                inputPassword.setError("Password must be filled with text")
            }else{
                inputPassword.setError(null)
            }
            if(inputUsername.getError() == null && inputPassword.getError() == null) checkLogin = true
            if(!checkLogin) return@OnClickListener
            else{
                login()
            }
        })
    }

    private fun login() {
        setLoading(true)
        val user = Auth(
            inputUsername.getEditText()?.getText().toString(),
            inputPassword.getEditText()?.getText().toString()
        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, UserApi.LOGIN, Response.Listener { response ->
                try {
                    val gson = Gson()
                    val jsonResponse = JSONObject(response)
                    val userResponse = gson.fromJson(jsonResponse.getString("data"), Member::class.java)
                    val userType = jsonResponse.getString("userType") // Get the userType as a string directly
                    Log.d("userType", userType)

                    if (userResponse != null) {
                        val userId = userResponse.id_member // Get the user ID from the response
                        Toast.makeText(this@MainActivity, "Berhasil Login", Toast.LENGTH_SHORT).show()

                        val sharedPreference = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.putInt("userId", userId) // Store the user ID in shared preferences
                        Log.d("userId", userId.toString())
                        editor.apply()

                        // Navigate to the appropriate activity based on the user type
                        when (userType) {
                            "pegawai" -> {
                                val moveHomePegawai = Intent(this@MainActivity, HomePegawaiActivity::class.java)
                                startActivity(moveHomePegawai)
                            }
                            "member" -> {
                                val moveHomeMember = Intent(this@MainActivity, HomeMemberActivity::class.java)
                                startActivity(moveHomeMember)
                            }
                            "instruktur" -> {
                                val moveHomeInstruktur = Intent(this@MainActivity, HomeInstrukturActivity::class.java)
                                startActivity(moveHomeInstruktur)
                            }
                        }
                    }
                    setLoading(false)
                } catch (e: Exception) {
                    setLoading(false)
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    Log.d("error", e.message.toString())
                }
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@MainActivity, "message", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

        // Menambahkan request ke request queue
        queue!!.add(stringRequest)
    }

    fun getBundle(){
        try{
            mbundle = intent?.getBundleExtra("register")!!
            if(mbundle != null){
                vUsername = mbundle.getString("username")!!
                vPassword = mbundle.getString("password")!!
            }else{

            }
        }catch (e: NullPointerException){
            vUsername = ""
            vPassword = ""
        }
    }

    fun setText() {
        etUsername = findViewById(R.id.etUsername)
        etUsername.setText(vUsername, TextView.BufferType.EDITABLE)
        etPassword = findViewById(R.id.etPassword)
        etPassword.setText(vPassword, TextView.BufferType.EDITABLE)
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.GONE
        }
    }
}