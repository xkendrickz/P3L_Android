package com.example.p3l_android.memberView.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.p3l_android.GuestActivity
import com.example.p3l_android.MainActivity
import com.example.p3l_android.R

class FragmentProfile : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val btnAbout: Button = view.findViewById(R.id.button3)
        val btnProfile: Button = view.findViewById(R.id.button4)

        btnLogout.setOnClickListener(){
            val backLogin = Intent(getActivity(), MainActivity::class.java)
            startActivity(backLogin)
        }

        btnAbout.setOnClickListener(){
            val toAbout = Intent(getActivity(), GuestActivity::class.java)
            startActivity(toAbout)
        }

        btnProfile.setOnClickListener(){
            val toProfile = Intent(getActivity(), MainActivity::class.java)
            startActivity(toProfile)
        }
    }
}