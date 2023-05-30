package com.example.p3l_android.memberView.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_android.R
import com.example.p3l_android.adapters.PresensiAdapter
import com.example.p3l_android.api.HistoryApi
import com.example.p3l_android.models.Presensi
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class FragmentHistoryPresensi : Fragment() {
    private var srPresensi: SwipeRefreshLayout? = null
    private var adapter: PresensiAdapter? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    private val myPreference = "myPref"
    private var userId: Int = -1

    companion object {
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history_presensi, container, false)

        queue = Volley.newRequestQueue(requireContext())
        layoutLoading = view.findViewById(R.id.layout_loading)
        srPresensi = view.findViewById(R.id.sr_presensi)

        val sharedPreference =
            requireContext().getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        userId = sharedPreference.getInt("userId", -1)

        srPresensi?.setOnRefreshListener {
            allPresensi()
        }

        val rvPresensi = view.findViewById<RecyclerView>(R.id.rv_presensi)
        adapter = PresensiAdapter(ArrayList(), requireContext())
        rvPresensi.layoutManager = LinearLayoutManager(requireContext())
        rvPresensi.adapter = adapter
        allPresensi()

        return view
    }

    private fun allPresensi() {
        srPresensi?.isRefreshing = true
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, HistoryApi.GET_HISTORY_PRESENSI + userId, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                val presensi: Array<Presensi> = gson.fromJson(jsonArray.toString(), Array<Presensi>::class.java)

                adapter?.setPresensiList(presensi)
                srPresensi?.isRefreshing = false

                if (!presensi.isEmpty())
                    Log.d("tes","Data Berhasil Diambil")
                else
                    Log.d("tes","Data Kosong!")
            },
            Response.ErrorListener { error ->
                srPresensi?.isRefreshing = false
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
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == Activity.RESULT_OK) {
            allPresensi()
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading?.visibility = View.VISIBLE
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading?.visibility = View.GONE
        }
    }
}