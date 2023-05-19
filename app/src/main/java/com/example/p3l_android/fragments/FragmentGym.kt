package com.example.p3l_android.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_android.MainActivity
import com.example.p3l_android.R
import com.example.p3l_android.api.BookingGymApi
import com.example.p3l_android.models.BookingGym
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class FragmentGym : Fragment() {
    companion object {
        private val SLOT_WAKTU = arrayOf(
            "07:00:00",
            "09:00:00",
            "11:00:00",
            "13:00:00",
            "15:00:00",
            "17:00:00",
            "19:00:00"
        )
    }

    private var etTanggal: EditText? = null
    private var edSlotWaktu: AutoCompleteTextView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    private val myPreference = "myPref"
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gym, container, false)
        queue = Volley.newRequestQueue(requireContext())
        etTanggal = view.findViewById(R.id.etTanggal)
        edSlotWaktu = view.findViewById(R.id.edSlotWaktu)
        layoutLoading = view.findViewById(R.id.layout_loading)
        val sharedPreference =
            requireContext().getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        userId = sharedPreference.getInt("userId", -1)

        setExposedDropDownMenu()

        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { requireActivity().finish() }
        val btnSave = view.findViewById<Button>(R.id.btn_save)
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)

        tvTitle.text = "Booking Gym"
        btnSave.setOnClickListener { createMahasiswa() }

        // Set click listener for the date EditText
        edSlotWaktu!!.setFocusable(false)
        etTanggal!!.setFocusable(false)
        etTanggal?.setOnClickListener {
            showDatePicker()
        }

        return view
    }

    private fun setExposedDropDownMenu() {
        val adapterFakultas: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_list,
            SLOT_WAKTU
        )
        edSlotWaktu!!.setAdapter(adapterFakultas)
    }

    private fun createMahasiswa() {
        setLoading(true)

        val bookingGym = BookingGym(
            userId,
            etTanggal!!.text.toString(),
            edSlotWaktu!!.text.toString(),
        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, BookingGymApi.ADD_URL,
                Response.Listener { response ->
                    val gson = Gson()
                    val bookingGym = gson.fromJson(response, BookingGym::class.java)

                    if (bookingGym != null)
                        Toast.makeText(
                            requireContext(),
                            "Data Berhasil Ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()

                    val returnIntent = Intent()
                    requireActivity().setResult(Activity.RESULT_OK, returnIntent)
                    requireActivity().finish()

                    setLoading(false)
                },
                Response.ErrorListener { error ->
                    setLoading(false)
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

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(bookingGym)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
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

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                etTanggal?.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
