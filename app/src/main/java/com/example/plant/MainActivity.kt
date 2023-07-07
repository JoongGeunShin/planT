package com.example.plant


import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.plant.main_fragment.CalendarFragment
import com.example.plant.main_fragment.HomeFragment
import com.example.plant.main_fragment.SettingsFragment
import com.example.plant.main_fragment.UserinfoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(){
    private val fl: FrameLayout by lazy {
        findViewById(R.id.mainFragmentContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //tet

        val bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)
        bnv_main.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.menu_home -> {
                        bnv_main.itemIconTintList =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        bnv_main.itemTextColor =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        HomeFragment()
                        // Respond to navigation item 1 click
                    }

                    R.id.menu_calendar -> {
                        bnv_main.itemIconTintList =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        bnv_main.itemTextColor =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        CalendarFragment()
                        // Respond to navigation item 2 click
                    }

                    R.id.menu_userinfo -> {
                        bnv_main.itemIconTintList =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        bnv_main.itemTextColor =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        UserinfoFragment()
                        // Respond to navigation item 3 click
                    }

                    else -> {
                        bnv_main.itemIconTintList =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        bnv_main.itemTextColor =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        SettingsFragment()
                    }
                }
            )
            true
        }
        bnv_main.selectedItemId = R.id.menu_home
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .commit()
    }



}