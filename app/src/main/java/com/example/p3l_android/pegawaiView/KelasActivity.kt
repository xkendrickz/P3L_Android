package com.example.p3l_android.pegawaiView

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
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
import com.example.p3l_android.adapters.KelasAdapter
import com.example.p3l_android.api.PresensiInstrukturApi
import com.example.p3l_android.models.Kelas
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class KelasActivity : AppCompatActivity() {
    private var srKelas: SwipeRefreshLayout? = null
    private var adapter: KelasAdapter? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object {
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelas)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srKelas = findViewById(R.id.sr_kelas)

        srKelas?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allMahasiwa() })

        val rvProduk = findViewById<RecyclerView>(R.id.rv_kelas)
        adapter = KelasAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allMahasiwa()
    }

    private fun allMahasiwa() {
        srKelas!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PresensiInstrukturApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                val kelas: Array<Kelas> = gson.fromJson(jsonArray.toString(), Array<Kelas>::class.java)

                adapter!!.setKelasList(kelas)
                srKelas!!.isRefreshing = false

                if (!kelas.isEmpty())
                    Toast.makeText(
                        this@KelasActivity,
                        "Data Berhasil Diambil!",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    Toast.makeText(this@KelasActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srKelas!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@KelasActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@KelasActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            // Menambahkan header pada request
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allMahasiwa()
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