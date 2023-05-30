package com.example.p3l_android.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_android.MainActivity
import com.example.p3l_android.R
import com.example.p3l_android.instrukturView.PresensiMember
import com.example.p3l_android.models.BookingKelas
import com.example.p3l_android.pegawaiView.PresensiInstrukturActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.ArrayList

class MemberAdapter (private var memberList: List<BookingKelas>, context: Context) :
    RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    private var filteredMemberList: MutableList<BookingKelas>
    private val context: Context

    init {
        filteredMemberList = ArrayList(memberList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_member, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return memberList.size

    }

    fun setMemberList(memberList: Array<BookingKelas>) {
        this.memberList = memberList.toList()
        filteredMemberList.clear()
        filteredMemberList.addAll(memberList.toList())
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = memberList[position]
        holder.tvMember.text = member.nama_member
        holder.tvInstruktur.text = member.nama_instruktur
        holder.tvKelas.text = member.nama_kelas
        holder.tvJenis.text = member.jenis
        val statusText = if (member.status == 1) "Hadir" else "Tidak Hadir"
        holder.tvStatus.text = statusText

        holder.btnStatus.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin mengupdate status booking ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Update") { _, _ ->
                    if (context is PresensiMember) member.id_booking_kelas?.let { it1 ->
                        context.updateStatus(
                            it1
                        )
                    }
                }
                .show()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMember: TextView
        var tvInstruktur: TextView
        var tvKelas: TextView
        var tvJenis: TextView
        var tvStatus: TextView
        var btnStatus: ImageButton

        init {
            tvMember = itemView.findViewById(R.id.tv_nama_member)
            tvInstruktur = itemView.findViewById(R.id.tv_nama_instruktur)
            tvKelas = itemView.findViewById(R.id.tv_nama_kelas)
            tvJenis = itemView.findViewById(R.id.tv_jenis)
            tvStatus = itemView.findViewById(R.id.tv_status)
            btnStatus = itemView.findViewById(R.id.btn_status)
        }
    }
}