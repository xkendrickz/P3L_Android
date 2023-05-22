package com.example.p3l_android.pegawaiView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_android.R
import com.example.p3l_android.api.PresensiInstrukturApi
import com.example.p3l_android.models.PresensiInstruktur
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PresensiInstrukturActivity : AppCompatActivity() {
    private var etMulaiKelas: EditText? = null
    private var etSelesaiKelas: EditText? = null
    private var timePickerMulaiKelas: TimePicker? = null
    private var timePickerSelesaiKelas: TimePicker? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    lateinit var mbundle : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presensi_instruktur)

        // Pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        timePickerMulaiKelas = findViewById(R.id.timePicker_mulai_kelas)
        timePickerSelesaiKelas = findViewById(R.id.timePicker_selesai_kelas)
        val tvNamaKelas = findViewById<TextView>(R.id.tv_presensi_kelas)
        val tvNamaInstruktur = findViewById<TextView>(R.id.tv_presensi_instruktur)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        mbundle = intent.getBundleExtra("id")!!
        tvNamaKelas.setText(mbundle.getString("nama_kelas"))
        tvNamaInstruktur.setText(mbundle.getString("nama_instruktur"))
        val id_jadwal_harian = mbundle.getInt("id_jadwal_harian")
        Log.d("jadwalharian",id_jadwal_harian.toString())

        btnSave.setOnClickListener { createPresensi(id_jadwal_harian) }
    }

    private fun createPresensi(id_jadwal_harian: Int) {
        // Fungsi untuk menambah data mahasiswa.
        setLoading(true)

        val mulaiKelasHour = timePickerMulaiKelas?.hour
        val mulaiKelasMinute = timePickerMulaiKelas?.minute
        val selesaiKelasHour = timePickerSelesaiKelas?.hour
        val selesaiKelasMinute = timePickerSelesaiKelas?.minute

        val mahasiswa = PresensiInstruktur(
            id_jadwal_harian,
            "$mulaiKelasHour:$mulaiKelasMinute",
            "$selesaiKelasHour:$selesaiKelasMinute"
        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, PresensiInstrukturApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var mahasiswa = gson.fromJson(response, PresensiInstruktur::class.java)

                if(mahasiswa != null)
                    Toast.makeText(this@PresensiInstrukturActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@PresensiInstrukturActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@PresensiInstrukturActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    val requestBody = gson.toJson(mahasiswa)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

        // Menambahkan request ke request queue
        queue!!.add(stringRequest)
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
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}