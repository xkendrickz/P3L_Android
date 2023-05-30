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
import com.example.p3l_android.adapters.TransaksiAdapter
import com.example.p3l_android.api.HistoryApi
import com.example.p3l_android.models.Transaksi
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class FragmentHistoryTransaksi : Fragment() {
    private var srTransaksi: SwipeRefreshLayout? = null
    private var adapter: TransaksiAdapter? = null
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
        val view = inflater.inflate(R.layout.fragment_history_transaksi, container, false)

        queue = Volley.newRequestQueue(requireContext())
        layoutLoading = view.findViewById(R.id.layout_loading)
        srTransaksi = view.findViewById(R.id.sr_transaksi)

        val sharedPreference =
            requireContext().getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        userId = sharedPreference.getInt("userId", -1)

        srTransaksi?.setOnRefreshListener {
            allTransaksi()
        }

        val rvTransaksi = view.findViewById<RecyclerView>(R.id.rv_transaksi)
        adapter = TransaksiAdapter(ArrayList(), requireContext())
        rvTransaksi.layoutManager = LinearLayoutManager(requireContext())
        rvTransaksi.adapter = adapter
        allTransaksi()

        return view
    }

    private fun allTransaksi() {
        srTransaksi?.isRefreshing = true
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, HistoryApi.GET_HISTORY_TRANSAKSI + userId, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                val transaksi: Array<Transaksi> = gson.fromJson(jsonArray.toString(), Array<Transaksi>::class.java)

                adapter?.setTransaksiList(transaksi)
                srTransaksi?.isRefreshing = false

                if (!transaksi.isEmpty())
                    Log.d("tes","Data Berhasil Diambil")
                else
                    Log.d("tes","Data Kosong!")
            },
            Response.ErrorListener { error ->
                srTransaksi?.isRefreshing = false
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
            allTransaksi()
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