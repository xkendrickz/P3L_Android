package com.example.p3l_android.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_android.R
import com.example.p3l_android.models.Presensi

class PresensiAdapter (private var presensiList: List<Presensi>, context: Context) :
    RecyclerView.Adapter<PresensiAdapter.ViewHolder>() {

    private var filteredMemberList: MutableList<Presensi>
    private val context: Context

    init {
        filteredMemberList = ArrayList(presensiList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_presensi, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return presensiList.size
    }

    fun setPresensiList(presensiList: Array<Presensi>) {
        this.presensiList = presensiList.toList()
        filteredMemberList.clear()
        filteredMemberList.addAll(presensiList.toList())
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val presensi = presensiList[position]
        holder.tvNamaAktivitas.text = presensi.nama_aktivitas
        holder.tvTanggal.text = presensi.tanggal
        holder.tvJenis.text = presensi.jenis
        holder.tvStatus.text = presensi.status
        holder.tvKelas.text = presensi.kelas
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNamaAktivitas: TextView
        var tvTanggal: TextView
        var tvJenis: TextView
        var tvStatus: TextView
        var tvKelas: TextView

        init {
            tvNamaAktivitas = itemView.findViewById(R.id.tv_nama_aktivitas)
            tvTanggal = itemView.findViewById(R.id.tv_tanggal)
            tvJenis = itemView.findViewById(R.id.tv_jenis)
            tvStatus = itemView.findViewById(R.id.tv_status)
            tvKelas = itemView.findViewById(R.id.tv_kelas)
        }
    }
}