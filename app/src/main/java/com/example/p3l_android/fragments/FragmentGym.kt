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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_android.R
import com.example.p3l_android.api.BookingGymApi
import com.example.p3l_android.models.BookingGym
import com.example.p3l_android.models.BookingGymCreate
import com.google.gson.Gson
import org.json.JSONException
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
    private var edTanggal2: AutoCompleteTextView? = null
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
        edTanggal2 = view.findViewById(R.id.edTanggal2)
        edSlotWaktu = view.findViewById(R.id.edSlotWaktu)
        layoutLoading = view.findViewById(R.id.layout_loading)
        val sharedPreference =
            requireContext().getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        userId = sharedPreference.getInt("userId", -1)

        getBookingGymDatesFromBackend().observe(viewLifecycleOwner, Observer { bookingGymDates ->
            // Lakukan tindakan dengan bookingGymDates di sini
            if (bookingGymDates != null) {
                // Contoh: Set adapter untuk edTanggal2 dropdown
                val adapterTanggal: ArrayAdapter<String> = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.item_list,
                    bookingGymDates
                )
                edTanggal2!!.setAdapter(adapterTanggal)
            }
        })
        setExposedDropDownMenu()

        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener {
            val selectedTanggal = edTanggal2?.text.toString()
            if (selectedTanggal.isNotEmpty()) {
                deleteBooking(selectedTanggal)
            } else {
                Toast.makeText(requireContext(), "Please select a Date", Toast.LENGTH_SHORT).show()
            }
        }
        val btnSave = view.findViewById<Button>(R.id.btn_save)

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

        val bookingGym = BookingGymCreate(
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
                            "Booking Berhasil Ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()
                    clearTextFields()
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
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
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

    private fun getBookingGymDatesFromBackend(): LiveData<ArrayList<String>> {
        val bookingGymDates = MutableLiveData<ArrayList<String>>()

        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, BookingGymApi.GET_BY_ID_URL + userId,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                val dates = ArrayList<String>()
                for (i in 0 until jsonArray.length()) {
                    val bookingGym = jsonArray.getJSONObject(i)
                    val tanggal = bookingGym.getString("tanggal")
                    dates.add(tanggal)
                }
                bookingGymDates.value = dates
            },
            Response.ErrorListener { error ->
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
        return bookingGymDates
    }

    private fun deleteBooking(tanggal: String) {
        setLoading(true)

        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, BookingGymApi.DELETE_URL + userId + "/" + tanggal, Response.Listener { response ->
                setLoading(false)
                Toast.makeText(requireContext(), "Booking successfully added", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
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
            // Add headers to the request
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun clearTextFields() {
        etTanggal?.setText("")
        edSlotWaktu?.setText("")
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
