package com.example.p3l_android.memberView

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.p3l_android.FragmentHome
import com.example.p3l_android.R
import com.example.p3l_android.memberView.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryMemberActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    lateinit var mbundle : Bundle
    lateinit var vKey : String
    private val myPreference = "myPref"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_member)

        getSupportActionBar()?.hide()
        changeFragment(FragmentHistoryTransaksi())
        getBundle()
        val sharedPreference = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        val usernameKey = sharedPreference!!.getString("username","")
        Log.d("tes",usernameKey!!)
        bottomNav = findViewById(R.id.bottom_navigation)
        Log.d("key",vKey)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_transaksi -> {
                    changeFragment(FragmentHistoryTransaksi())
                    true
                }
                R.id.menu_presensi -> {
                    changeFragment(FragmentHistoryPresensi())
                    true
                }
                else -> false
            }
        }
    }

    fun getBundle(){
        try{
            mbundle = intent?.getBundleExtra("key")!!
            if(mbundle != null){
                vKey = mbundle.getString("username")!!
            }else{

            }
        }catch (e: NullPointerException){
            vKey = ""
        }
    }

    fun changeFragment(fragment: Fragment?){
        if(fragment != null){
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layoutFragment, fragment)
                .commit()
        }
    }
}