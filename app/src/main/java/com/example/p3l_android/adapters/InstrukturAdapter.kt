package com.example.p3l_android.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_android.R
import com.example.p3l_android.models.Instruktur

class InstrukturAdapter (private var instrukturList: List<Instruktur>, context: Context) :
    RecyclerView.Adapter<InstrukturAdapter.ViewHolder>() {

    private var filteredMemberList: MutableList<Instruktur>
    private val context: Context

    init {
        filteredMemberList = ArrayList(instrukturList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_instruktur, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return instrukturList.size

    }

    fun setInstrukturList(instrukturList: Array<Instruktur>) {
        this.instrukturList = instrukturList.toList()
        filteredMemberList.clear()
        filteredMemberList.addAll(instrukturList.toList())
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val instruktur = instrukturList[position]
        holder.tv_nama_kelas.text = instruktur.nama_kelas
        holder.tv_tanggal.text = instruktur.hari
        holder.tv_detail_izin.text = instruktur.izin
        holder.tv_mulai_kelas.text = instruktur.mulai_kelas
        holder.tv_selesai_kelas.text = instruktur.selesai_kelas
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_nama_kelas: TextView
        var tv_tanggal: TextView
        var tv_detail_izin: TextView
        var tv_mulai_kelas: TextView
        var tv_selesai_kelas: TextView

        init {
            tv_nama_kelas = itemView.findViewById(R.id.tv_nama_kelas)
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal)
            tv_detail_izin = itemView.findViewById(R.id.tv_detail_izin)
            tv_mulai_kelas = itemView.findViewById(R.id.tv_mulai_kelas)
            tv_selesai_kelas = itemView.findViewById(R.id.tv_selesai_kelas)
        }
    }
}