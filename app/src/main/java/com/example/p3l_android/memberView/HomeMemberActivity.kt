package com.example.p3l_android.memberView

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.p3l_android.R
import com.example.p3l_android.memberView.fragments.FragmentClass
import com.example.p3l_android.memberView.fragments.FragmentGym
import com.example.p3l_android.FragmentHome
import com.example.p3l_android.memberView.fragments.FragmentProfileMember
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeMemberActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    lateinit var mbundle : Bundle
    lateinit var vKey : String
    private val myPreference = "myPref"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_member)

        getSupportActionBar()?.hide()
        changeFragment(FragmentHome())
        getBundle()
        val sharedPreference = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        val usernameKey = sharedPreference!!.getString("username","1")
        Log.d("tes",usernameKey!!)
        bottomNav = findViewById(R.id.bottom_navigation)
        Log.d("key",vKey)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    changeFragment(FragmentHome())
                    true
                }
                R.id.menu_gym -> {
                    changeFragment(FragmentGym())
                    true
                }
                R.id.menu_class -> {
                    changeFragment(FragmentClass())
                    true
                }
                R.id.menu_profile ->{
                    supportFragmentManager.beginTransaction().replace(R.id.layoutFragment, FragmentProfileMember())
                        .commit()
                    changeFragment(FragmentProfileMember())
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