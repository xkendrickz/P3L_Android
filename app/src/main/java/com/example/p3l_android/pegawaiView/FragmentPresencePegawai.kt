package com.example.p3l_android.pegawaiView

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.RequestQueue
import com.example.p3l_android.R
import com.example.p3l_android.adapters.KelasAdapter

class FragmentPresencePegawai : Fragment() {
//    private var srKelas: SwipeRefreshLayout? = null
//    private var adapter: KelasAdapter? = null
//    private var layoutLoading: LinearLayout? = null
//    private var queue: RequestQueue? = null
//
//    companion object {
//        const val LAUNCH_ADD_ACTIVITY = 123
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_presence_pegawai, container, false)
        return view
//        queue = Volley.newRequestQueue(requireContext())
//        layoutLoading = view.findViewById(R.id.layout_loading)
//        srKelas = view.findViewById(R.id.sr_kelas)
//
//        srKelas?.setOnRefreshListener { SwipeRefreshLayout.OnRefreshListener { allKelas()
//            Log.d("ter","ter") } }
//
//        val rvKelas = view.findViewById<RecyclerView>(R.id.rv_kelas)
//        adapter = KelasAdapter(ArrayList(), requireContext())
//        rvKelas.layoutManager = LinearLayoutManager(requireContext())
//        rvKelas.adapter = adapter
//        allKelas()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnKelas: Button = view.findViewById(R.id.btnKelas_activity)

        btnKelas.setOnClickListener(){
            val toKelas = Intent(getActivity(), KelasActivity::class.java)
            startActivity(toKelas)
        }
    }

//    private fun allKelas() {
//        srKelas?.isRefreshing = true
//        val stringRequest: StringRequest = object : StringRequest(Method.GET, PresensiInstrukturApi.GET_ALL_URL, Response.Listener { response ->
//            val gson = Gson()
//            val jsonObject = JSONObject(response)
//            val jsonArray = jsonObject.getJSONArray("data")
//            val kelas: Array<Kelas> = gson.fromJson(jsonArray.toString(), Array<Kelas>::class.java)
//
//            adapter?.setKelasList(kelas)
//            srKelas?.isRefreshing = false
//
//            if (!kelas.isEmpty())
//                Log.d("tes","Data Berhasil Diambil")
////                Toast.makeText(requireContext(), "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
//            else
//                Log.d("tes","Data Kosong!")
////                Toast.makeText(requireContext(), "Data Kosong!", Toast.LENGTH_SHORT).show()
//        }, Response.ErrorListener { error ->
//            srKelas?.isRefreshing = false
//            try {
//                val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                val errors = JSONObject(responseBody)
////                Toast.makeText(requireContext(), errors.getString("message"), Toast.LENGTH_SHORT).show()
//            } catch (e: Exception) {
////                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
//            }
//        }) {
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val headers = HashMap<String, String>()
//                headers["Accept"] = "application/json"
//                return headers
//            }
//        }
//        queue?.add(stringRequest)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == Activity.RESULT_OK) {
//            allKelas()
//        }
//    }
//
//    private fun setLoading(isLoading: Boolean) {
//        if (isLoading) {
//            activity?.window?.setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//            )
//            layoutLoading?.visibility = View.VISIBLE
//        } else {
//            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            layoutLoading?.visibility = View.GONE
//        }
//    }
}