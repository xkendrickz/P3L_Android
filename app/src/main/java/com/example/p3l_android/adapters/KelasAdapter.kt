package com.example.p3l_android.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_android.MainActivity
import com.example.p3l_android.R
import com.example.p3l_android.models.Kelas
import com.example.p3l_android.pegawaiView.FragmentPresencePegawai
import com.example.p3l_android.pegawaiView.KelasActivity
import com.example.p3l_android.pegawaiView.KelasActivity.Companion.LAUNCH_ADD_ACTIVITY
import com.example.p3l_android.pegawaiView.PresensiInstrukturActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class KelasAdapter (private var kelasList: List<Kelas>, context: Context) :
    RecyclerView.Adapter<KelasAdapter.ViewHolder>() {

    private var filteredKelasList: MutableList<Kelas>
    private val context: Context

    init {
        filteredKelasList = ArrayList(kelasList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_kelas, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return kelasList.size

    }

    fun setKelasList(kelasList: Array<Kelas>) {
        this.kelasList = kelasList.toList()
        filteredKelasList.clear()
        filteredKelasList.addAll(kelasList.toList())
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kelas = kelasList[position]
        holder.tvKelas.text = kelas.nama_kelas
        holder.tvInstruktur.text = kelas.nama_instruktur

        holder.cvKelas.setOnClickListener {
            val i = Intent(context, PresensiInstrukturActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("nama_instruktur", kelas.nama_instruktur)
            mBundle.putString("nama_kelas", kelas.nama_kelas)
            mBundle.putInt("id_jadwal_harian", kelas.id_jadwal_harian)
            i.putExtra("id", mBundle)
            if (context is KelasActivity)
                context.startActivityForResult(i, KelasActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvKelas: TextView
        var tvInstruktur: TextView
        var cvKelas: CardView

        init {
            tvKelas = itemView.findViewById(R.id.tv_nama_kelas)
            tvInstruktur = itemView.findViewById(R.id.tv_nama_instruktur)
            cvKelas = itemView.findViewById(R.id.cv_kelas)
        }
    }
}