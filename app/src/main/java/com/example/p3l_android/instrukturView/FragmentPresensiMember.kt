package com.example.p3l_android.instrukturView

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.p3l_android.R

class FragmentPresensiMember : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_presensi_member, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnKelas: Button = view.findViewById(R.id.btnMember_activity)

        btnKelas.setOnClickListener(){
            val toKelas = Intent(getActivity(), PresensiMember::class.java)
            startActivity(toKelas)
        }
    }
}