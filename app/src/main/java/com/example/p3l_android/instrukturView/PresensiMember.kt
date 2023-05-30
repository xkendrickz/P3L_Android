package com.example.p3l_android.instrukturView

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_android.R
import com.example.p3l_android.adapters.MemberAdapter
import com.example.p3l_android.api.PresensiInstrukturApi
import com.example.p3l_android.api.PresensiMemberApi
import com.example.p3l_android.models.BookingGym
import com.example.p3l_android.models.BookingKelas
import com.example.p3l_android.models.Member
import com.example.p3l_android.models.PresensiInstruktur
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PresensiMember : AppCompatActivity() {
    private var srMember: SwipeRefreshLayout? = null
    private var adapter: MemberAdapter? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    private val myPreference = "myPref"
    private var userId: Int = -1

    companion object {
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presensi_member)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srMember = findViewById(R.id.sr_member)
        val sharedPreference = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        userId = sharedPreference.getInt("userId", -1)
        Log.d("idpre",userId.toString())

        srMember?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allMember() })

        val rvProduk = findViewById<RecyclerView>(R.id.rv_member)
        adapter = MemberAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allMember()
    }

    private fun allMember() {
        srMember!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PresensiMemberApi.GET_BY_ID_URL + userId, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                val member: Array<BookingKelas> = gson.fromJson(jsonArray.toString(), Array<BookingKelas>::class.java)

                adapter!!.setMemberList(member)
                srMember!!.isRefreshing = false

                if (!member.isEmpty())
                    Toast.makeText(
                        this@PresensiMember,
                        "Data Berhasil Diambil!",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    Toast.makeText(this@PresensiMember, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srMember!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@PresensiMember,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@PresensiMember, e.message, Toast.LENGTH_SHORT).show()
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

    fun updateStatus(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, PresensiMemberApi.UPDATE_URL + id, Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var mahasiswa = gson.fromJson(response, BookingGym::class.java)
                if(mahasiswa != null)
                    Toast.makeText(this@PresensiMember, "Status berhasil diupdate", Toast.LENGTH_SHORT).show()
                allMember()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Log.d("message",errors.getString("message"))
                    Toast.makeText(
                        this@PresensiMember,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: java.lang.Exception) {
                    Toast.makeText(this@PresensiMember, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            // Menambahkan header pada request
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allMember()
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