package com.example.p3l_android.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_android.R
import com.example.p3l_android.instrukturView.PresensiMember
import com.example.p3l_android.models.Transaksi
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TransaksiAdapter (private var transaksiList: List<Transaksi>, context: Context) :
    RecyclerView.Adapter<TransaksiAdapter.ViewHolder>() {

    private var filteredMemberList: MutableList<Transaksi>
    private val context: Context

    init {
        filteredMemberList = ArrayList(transaksiList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_transaksi, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transaksiList.size

    }

    fun setTransaksiList(transaksiList: Array<Transaksi>) {
        this.transaksiList = transaksiList.toList()
        filteredMemberList.clear()
        filteredMemberList.addAll(transaksiList.toList())
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaksi = transaksiList[position]
        holder.tvNamaAktivitas.text = transaksi.nama_aktivitas
        holder.tvTanggal.text = transaksi.tanggal
        holder.tvHarga.text = transaksi.harga
        holder.tvJenis.text = transaksi.kelas
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNamaAktivitas: TextView
        var tvTanggal: TextView
        var tvHarga: TextView
        var tvJenis: TextView

        init {
            tvNamaAktivitas = itemView.findViewById(R.id.tv_nama_aktivitas)
            tvTanggal = itemView.findViewById(R.id.tv_tanggal)
            tvHarga = itemView.findViewById(R.id.tv_harga)
            tvJenis = itemView.findViewById(R.id.tv_jenis)
        }
    }
}