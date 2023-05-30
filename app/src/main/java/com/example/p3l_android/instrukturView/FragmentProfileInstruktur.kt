package com.example.p3l_android.instrukturView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_android.MainActivity
import com.example.p3l_android.R
import com.example.p3l_android.api.MemberApi
import com.example.p3l_android.memberView.HistoryMemberActivity
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.HashMap


class FragmentProfileInstruktur : Fragment() {
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    private var tvNamaProfilMember: TextView? = null
    private var tvTglLahirProfilMember: TextView? = null
    private var tvWaktuTerlambat: TextView? = null
    private val myPreference = "myPref"
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_instruktur, container, false)
        queue = Volley.newRequestQueue(requireContext())
        layoutLoading = view.findViewById(R.id.layout_loading)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val btnHistory: Button = view.findViewById(R.id.button4)

        tvNamaProfilMember = view.findViewById(R.id.tvNamaProfilInstruktur)
        tvTglLahirProfilMember = view.findViewById(R.id.tvTglLahirProfilInstruktur)
        tvWaktuTerlambat = view.findViewById(R.id.tvWaktuTerlambat)

        btnLogout.setOnClickListener(){
            val backLogin = Intent(getActivity(), MainActivity::class.java)
            startActivity(backLogin)
        }

        btnHistory.setOnClickListener(){
            val toHistory = Intent(getActivity(), HistoryInstrukturActivity::class.java)
            startActivity(toHistory)
        }

        val sharedPreference =
            requireContext().getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        userId = sharedPreference.getInt("userId", -1)
        setText()
        return view
    }

    private fun setText() {
//        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, MemberApi.GET_PROFILE_INSTRUKTUR + userId, Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val item = jsonObject.getJSONObject("data")
                tvNamaProfilMember!!.text = item.getString("nama_instruktur")
                tvTglLahirProfilMember!!.text = item.getString("tanggal_lahir")
                tvWaktuTerlambat!!.text = item.getString("waktu_terlambat")

//                Toast.makeText(requireContext(), "Data Berhasil diambil!", Toast.LENGTH_SHORT)
//                    .show()
//                setLoading(false)
            }, Response.ErrorListener { error ->
//                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        requireContext(),
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }


    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}