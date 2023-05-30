package com.example.p3l_android.memberView.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_android.GuestActivity
import com.example.p3l_android.MainActivity
import com.example.p3l_android.R
import com.example.p3l_android.api.BookingGymApi
import com.example.p3l_android.api.MemberApi
import com.example.p3l_android.memberView.HistoryMemberActivity
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.ArrayList
import java.util.HashMap

class FragmentProfileMember : Fragment() {
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    private var tvNamaProfilMember: TextView? = null
    private var tvAlamatProfilMember: TextView? = null
    private var tvTglLahirProfilMember: TextView? = null
    private var tvTeleponProfilMember: TextView? = null
    private var tvEmailProfilMember: TextView? = null
    private var tvMasaBerlakuProfilMember: TextView? = null
    private var tvJenisPaketBerlakuProfilMember: TextView? = null
    private var tvDepositPaketBerlakuProfilMember: TextView? = null
    private var tvDepositRegulerBerlakuProfilMember: TextView? = null
    private val myPreference = "myPref"
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_member, container, false)
        queue = Volley.newRequestQueue(requireContext())
        layoutLoading = view.findViewById(R.id.layout_loading)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val btnHistory: Button = view.findViewById(R.id.button4)

        tvNamaProfilMember = view.findViewById(R.id.tvNamaProfilMember)
        tvAlamatProfilMember = view.findViewById(R.id.tvAlamatProfilMember)
        tvTglLahirProfilMember = view.findViewById(R.id.tvTglLahirProfilMember)
        tvTeleponProfilMember = view.findViewById(R.id.tvTeleponProfilMember)
        tvEmailProfilMember = view.findViewById(R.id.tvEmailProfilMember)
        tvMasaBerlakuProfilMember = view.findViewById(R.id.tvMasaBerlakuProfilMember)
        tvJenisPaketBerlakuProfilMember = view.findViewById(R.id.tvJenisPaketBerlakuProfilMember)
        tvDepositPaketBerlakuProfilMember = view.findViewById(R.id.tvDepositPaketBerlakuProfilMember)
        tvDepositRegulerBerlakuProfilMember = view.findViewById(R.id.tvDepositRegulerBerlakuProfilMember)

        btnLogout.setOnClickListener(){
            val backLogin = Intent(getActivity(), MainActivity::class.java)
            startActivity(backLogin)
        }

        btnHistory.setOnClickListener(){
            val toHistory = Intent(getActivity(), HistoryMemberActivity::class.java)
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
            StringRequest(Method.GET, MemberApi.GET_PROFILE_DATA + userId, Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val item = jsonObject.getJSONObject("data")
                tvNamaProfilMember!!.text = item.getString("nama_member")
                tvAlamatProfilMember!!.text = item.getString("alamat")
                tvTglLahirProfilMember!!.text = item.getString("tanggal_lahir")
                tvTeleponProfilMember!!.text = item.getString("telepon")
                tvEmailProfilMember!!.text = item.getString("email")
                tvMasaBerlakuProfilMember!!.text = item.getString("masa_aktif")
                tvJenisPaketBerlakuProfilMember!!.text = item.getString("nama_kelas")
                tvDepositPaketBerlakuProfilMember!!.text = item.getString("sisa_deposit_paket")
                tvDepositRegulerBerlakuProfilMember!!.text = item.getString("sisa_deposit_reguler")

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