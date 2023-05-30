package com.example.p3l_android.instrukturView

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_android.R
import com.example.p3l_android.adapters.InstrukturAdapter
import com.example.p3l_android.api.HistoryApi
import com.example.p3l_android.models.Instruktur
import com.example.p3l_android.pegawaiView.KelasActivity
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class HistoryInstrukturActivity : AppCompatActivity() {
    private var srInstruktur: SwipeRefreshLayout? = null
    private var adapter: InstrukturAdapter? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    private val myPreference = "myPref"
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_instruktur)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srInstruktur = findViewById(R.id.sr_instruktur)

        val sharedPreference = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        userId = sharedPreference.getInt("userId", -1)

        srInstruktur?.setOnRefreshListener {
            allInstruktur()
        }

        val rvInstruktur = findViewById<RecyclerView>(R.id.rv_instruktur)
        adapter = InstrukturAdapter(ArrayList(),this)
        rvInstruktur.layoutManager = LinearLayoutManager(this)
        rvInstruktur.adapter = adapter
        allInstruktur()
    }

    private fun allInstruktur() {
        srInstruktur?.isRefreshing = true
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, HistoryApi.GET_HISTORY_INSTRUKTUR + userId, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                val instruktur: Array<Instruktur> = gson.fromJson(jsonArray.toString(), Array<Instruktur>::class.java)

                adapter?.setInstrukturList(instruktur)
                srInstruktur?.isRefreshing = false

                if (!instruktur.isEmpty())
                    Log.d("tes","Data Berhasil Diambil")
                else
                    Log.d("tes","Data Kosong!")
            },
            Response.ErrorListener { error ->
                srInstruktur?.isRefreshing = false
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                } catch (e: Exception) {
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue?.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KelasActivity.LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) {
            allInstruktur()
        }
    }

    // Fungsi ini digunakan menampilkan layout loading
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